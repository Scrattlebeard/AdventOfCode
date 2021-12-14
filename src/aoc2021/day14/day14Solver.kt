package aoc2021.day14

import lib.*

suspend fun main() {
    challenge<Pair<String, Map<String, Char>>> {

        day(14)

        input("example.txt")
        input("input.txt")

        parser {
            val inp = it.getStringsGroupedByEmptyLine()
            val rules = inp[1].map { it.split(" -> ") }.associate { it[0] to it[1][0] }
            inp.first().first() to rules
        }

        solver {
            println("Solving challenge 1...")
            solve(it, 10).toString()
        }

        solver {
            println("Solving challenge 2...")
            solve(it, 40).toString()
        }
    }
}

fun String.getOccurences(): Map<Char, Long> {
    val res = mutableMapOf<Char, Long>()
    this.forEach { res[it] = res.getOrDefault(it, 0L) + 1 }
    return res
}

fun solve(inp: Pair<String, Map<String, Char>>, steps: Int): Long {
    val solutions = mutableMapOf<String, Map<Char, Long>>()
    val parts = inp.first.windowed(2)
    val res = parts
        .map { solvePart(it, steps, inp.second, solutions) }
        .fold(mapOf<Char, Long>()) { acc, map -> acc.elementwiseSum(map)}.toMutableMap()

    inp.first.subSequence(1, inp.first.length - 1).forEach { res[it] = res[it]!! - 1 }
    return (res.entries.maxOf { it.value } - res.entries.minOf { it.value })
}

fun solvePart(chars: String, steps: Int, rules: Map<String, Char>, solutions: MutableMap<String, Map<Char, Long>>): Map<Char, Long> {
    if (steps < 1) {
        return chars.getOccurences()
    }

    val solutionKey = "${chars}$steps"
    if (!solutions.containsKey(solutionKey)) {
        val leftRes = solvePart("${chars[0]}${rules[chars]}", steps - 1, rules, solutions)
        val rightRes = solvePart("${rules[chars]}${chars[1]}", steps - 1, rules, solutions)

        val tempRes = leftRes.elementwiseSum(rightRes).toMutableMap()
        tempRes[rules[chars]!!] = tempRes[rules[chars]!!]!! - 1
        solutions[solutionKey] = tempRes
    }

    return solutions[solutionKey]!!
}

fun <T> Map<T, Long>.elementwiseSum(other: Map<T, Long>): Map<T, Long> {
    val res = mutableMapOf<T, Long>()
    this.forEach {
        res[it.key] = res.getOrDefault(it.key, 0L) + it.value
    }
    other.forEach {
        res[it.key] = res.getOrDefault(it.key, 0L) + it.value
    }
    return res
}