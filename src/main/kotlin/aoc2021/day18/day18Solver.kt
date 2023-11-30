package aoc2021.day18

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<SnailfishNumber>> {
    return setup {
        day(18)
        year(2021)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .map { SnailfishNumber(it) }
        }

        partOne {
            val res = it.subList(1, it.size)
                .fold(it.first()) { acc, number ->
                    acc.plus(number)
                }
            println("Result is: ${res.getRepr()}")
            res.magnitude()
                .toString()
        }

        partTwo {
            var largest = -1L
            for (i in it.indices) {
                for (j in it.indices) {
                    if (j == i) continue
                    val mag = it[i].plus(it[j]).magnitude()
                    largest = maxOf(largest, mag)
                }
            }
            largest.toString()
        }
    }
}

fun String.findSeperator(): Int {
    var openCount = 0
    for (i in this.indices) {
        when (this[i]) {
            '[' -> openCount++
            ']' -> openCount--
            ',' -> if (openCount == 0) {
                return i
            }

            else -> {}
        }
        if (openCount < 0) {
            throw Exception("bracket mismatch")
        }
    }
    throw Exception("not found")
}

interface SnailfishElement {
    fun magnitude(): Long
    fun tryExplode(depth: Int): Pair<SnailfishElement, Pair<Long?, Long?>?>
    fun trySplit(): Pair<SnailfishElement, Boolean>
    fun addRight(number: Long?): SnailfishElement
    fun addLeft(number: Long?): SnailfishElement
    fun getRepr(): String

    companion object {
        fun parseRepr(repr: String): SnailfishElement {
            return if (repr.first() == '[') {
                SnailfishNumber(repr)
            } else {
                RegularNumber(repr)
            }
        }
    }
}

class SnailfishNumber() : SnailfishElement {

    var contents: Pair<SnailfishElement, SnailfishElement> = RegularNumber("0") to RegularNumber("0")

    constructor(data: Pair<SnailfishElement, SnailfishElement>) : this() {
        contents = data
    }

    constructor(repr: String) : this() {
        val innerRepr = repr.substring(1, repr.length - 1)
        val sep = innerRepr.findSeperator()

        contents = SnailfishElement.parseRepr(innerRepr.substring(0, sep)) to
                SnailfishElement.parseRepr(innerRepr.substring(sep + 1, innerRepr.length))
    }

    fun plus(other: SnailfishNumber): SnailfishNumber {
        val res = SnailfishNumber(this to other)
        res.reduce()
        return res
    }

    fun reduce() {
        //println("Reducing ${this.getRepr()}")
        var done = false
        while (!done) {
            var explodeRes = contents.first.tryExplode(1)
            if (explodeRes.second != null) {
                contents = explodeRes.first to contents.second.addLeft(explodeRes.second?.second)
                continue
            }
            explodeRes = contents.second.tryExplode(1)
            if (explodeRes.second != null) {
                contents = contents.first.addRight(explodeRes.second?.first) to explodeRes.first
                continue
            }
            var splitRes = contents.first.trySplit()
            if (splitRes.second) {
                contents = splitRes.first to contents.second
                continue
            }
            splitRes = contents.second.trySplit()
            if (splitRes.second) {
                contents = contents.first to splitRes.first
                continue
            }
            done = true
        }
    }

    override fun magnitude(): Long {
        return 3 * contents.first.magnitude() + 2 * contents.second.magnitude()
    }

    override fun tryExplode(depth: Int): Pair<SnailfishElement, Pair<Long?, Long?>?> {
        if (depth > 3) {
            return RegularNumber(0) to (contents.first.magnitude() to contents.second.magnitude())
        } else {
            var explodeRes = contents.first.tryExplode(depth + 1)
            if (explodeRes.second != null) {
                return SnailfishNumber(explodeRes.first to contents.second.addLeft(explodeRes.second?.second)) to (explodeRes.second?.first to null)
            }
            explodeRes = contents.second.tryExplode(depth + 1)
            if (explodeRes.second != null) {
                return SnailfishNumber(contents.first.addRight(explodeRes.second?.first) to explodeRes.first) to (null to explodeRes.second?.second)
            }
        }
        return this to null
    }

    override fun addLeft(number: Long?): SnailfishElement {
        if (number == null) return this
        return SnailfishNumber(contents.first.addLeft(number) to contents.second)
    }

    override fun addRight(number: Long?): SnailfishElement {
        if (number == null) return this
        return SnailfishNumber(contents.first to contents.second.addRight(number))
    }

    override fun trySplit(): Pair<SnailfishElement, Boolean> {
        var res = contents.first.trySplit()
        if (res.second) {
            return SnailfishNumber(res.first to contents.second) to true
        }
        res = contents.second.trySplit()
        if (res.second) {
            return SnailfishNumber(contents.first to res.first) to true
        }
        return this to false
    }

    override fun getRepr(): String {
        return "[${contents.first.getRepr()},${contents.second.getRepr()}]"
    }
}

class RegularNumber(val value: Long) : SnailfishElement {

    constructor(repr: String) : this(repr.toLong())

    override fun magnitude(): Long {
        return value
    }

    override fun tryExplode(depth: Int): Pair<SnailfishElement, Pair<Long?, Long?>?> {
        return this to null
    }

    override fun addLeft(number: Long?): SnailfishElement {
        return RegularNumber(value + (number ?: 0))
    }

    override fun addRight(number: Long?): SnailfishElement {
        return RegularNumber(value + (number ?: 0))
    }

    override fun trySplit(): Pair<SnailfishElement, Boolean> {
        if (value > 9) {
            return SnailfishNumber(RegularNumber("${value / 2}") to RegularNumber("${value / 2 + value % 2}")) to true
        }
        return this to false
    }

    override fun getRepr(): String {
        return "$value"
    }
}