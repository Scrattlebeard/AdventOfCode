package aoc2023.day19

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<Map<String, Workflow>, List<Part>>> {
    return setup {
        day(19)
        year(2023)

        //input("example.txt")

        parser {
            val inputGroups = it.getStringsGroupedByEmptyLine()
            inputGroups.first().map { it.toWorkflow() }.associateBy { it.key } to inputGroups.last().map { it.toPart() }
        }

        partOne {
            workflows = it.first
            val accepted = it.second.filter { workflows["in"]!!.run(it) }
            accepted.sumOf { it.x + it.m + it.a + it.s }.toString()
        }

        partTwo {
            workflows = it.first
            val accepted = workflows["in"]!!.run(PartRange(1..4000, 1..4000, 1..4000, 1..4000))
            accepted.sumOf { it.size() }.toString()

            //135056247928947 too high
        }
    }
}

var workflows: Map<String, Workflow> = mapOf()

data class Part(val x: Long, val m: Long, val a: Long, val s: Long)

fun String.toPart(): Part {
    val values = this.replace("{", "").replace("}", "").split(",")
    val x = values[0].replace("x=", "").toLong()
    val m = values[1].replace("m=", "").toLong()
    val a = values[2].replace("a=", "").toLong()
    val s = values[3].replace("s=", "").toLong()
    return Part(x, m, a, s)
}

data class Workflow(val key: String, val criteria: List<Criteria>, val default: String) {
    fun run(part: Part): Boolean {
        this.criteria.forEach {
            val res = it.run(part)
            if (res != null) {
                return res.tryAccept(part)
            }
        }
        return this.default.getResult().tryAccept(part)
    }

    fun run(parts: PartRange): List<PartRange> {
        val res = mutableListOf<PartRange>()
        var remaining = listOf(parts)

        this.criteria.forEach {criteria ->
            val newRemaining = mutableListOf<PartRange>()
            remaining.forEach {
                val eval = criteria.run(it)
                res.addAll(eval.first)
                newRemaining.add(eval.second)
            }
            remaining = newRemaining
        }

        if(remaining.isNotEmpty()) {
            remaining.forEach {
                res.addAll(default.getResult().tryAccept(it))
            }
        }

        return res
    }
}

fun String.toWorkflow(): Workflow {
    val groups = this.replace("}", "").split("{")
    val workflowStrings = groups.last().split(",")
    val defaultRes = workflowStrings.last()
    return Workflow(groups.first(), workflowStrings.dropLast(1).map { it.getCriteria() }, defaultRes)
}

interface WorkflowResult {
    fun tryAccept(part: Part): Boolean
    fun tryAccept(parts: PartRange): List<PartRange>
}

data class StaticResult(val isAccepted: Boolean) : WorkflowResult {
    override fun tryAccept(part: Part): Boolean {
        return isAccepted
    }
    override fun tryAccept(parts: PartRange): List<PartRange> {
        return if (isAccepted) listOf(parts) else listOf()
    }
}

class ComputedResult(val key: String) : WorkflowResult {
    override fun tryAccept(part: Part): Boolean {
        return workflows[key]!!.run(part)
    }
    override fun tryAccept(parts: PartRange): List<PartRange> {
        return workflows[key]!!.run(parts)
    }
}

data class Criteria(val category: Char, val number: Int, val acceptsGreaterThan: Boolean, val whenAccept: String) {
    fun run(part: Part): WorkflowResult? {
        val partValue = when (category) {
            'x' -> part.x
            'm' -> part.m
            'a' -> part.a
            's' -> part.s
            else -> throw Exception("Unknown category from criteria $this")
        }
        val isPassed = if (acceptsGreaterThan) partValue > number else partValue < number
        return if (isPassed) whenAccept.getResult() else null
    }

    fun run(parts: PartRange): Pair<List<PartRange>, PartRange> {

        if (parts.isEmpty()) {
            return (listOf<PartRange>() to parts)
        }

        val splitNumber = if (acceptsGreaterThan) number + 1 else number
        val ranges = when (category) {
            'x' -> parts.copy(x = parts.x.stopAt(splitNumber - 1)) to parts.copy(x = parts.x.startAt(splitNumber))
            'm' -> parts.copy(m = parts.m.stopAt(splitNumber - 1)) to parts.copy(m = parts.m.startAt(splitNumber))
            'a' -> parts.copy(a = parts.a.stopAt(splitNumber - 1)) to parts.copy(a = parts.a.startAt(splitNumber))
            's' -> parts.copy(s = parts.s.stopAt(splitNumber - 1)) to parts.copy(s = parts.s.startAt(splitNumber))
            else -> throw Exception("Unknown category from criteria $this")
        }

       return if(acceptsGreaterThan) {
           whenAccept.getResult().tryAccept(ranges.second) to ranges.first
       } else {
           whenAccept.getResult().tryAccept(ranges.first) to ranges.second
       }
    }
}

fun String.getCriteria(): Criteria {
    val split = this.split(":")
    val number = split.first().drop(2).toInt()
    val isGreater = split.first()[1] == '>'
    return Criteria(this.first(), number, isGreater, split.last())
}

fun String.getResult(): WorkflowResult {
    return if (this.length == 1) StaticResult(this == "A")
    else ComputedResult(this)
}

data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
    fun isEmpty(): Boolean {
        return listOf(x, m, a, s).all { it.isEmpty() }
    }

    fun size(): Long {
        return x.size().toLong() * m.size() * a.size() * s.size()
    }
}