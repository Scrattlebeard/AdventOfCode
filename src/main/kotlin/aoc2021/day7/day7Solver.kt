package aoc2021.day7

import lib.*
import kotlin.math.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Int>> {
    return setup {
        day(7)
        year(2021)

        input("example.txt")
        input("input.txt")

        parser {
            it.readText()
                .trim()
                .split(',')
                .map { s -> s.toInt() }
        }

        partOne {
            val mean = it.sorted()[it.size / 2]
            it.sumOf { depth -> abs(depth - mean) }.toString()
        }

        partTwo {
            val avg = floor(it.average()).roundToInt()
            it.sumOf { depth -> abs(depth - avg).sumOfIntsUntil() }.toString()
        }
    }
}

fun Int.sumOfIntsUntil(): Long {
    return (this * (this + 1).toDouble() / 2).roundToLong()
}

fun costOf(depth: Int, depths: List<Int>): Long {
    return depths.sumOf { abs(it - depth).sumOfIntsUntil() }
}

fun solvePartTwo(input: List<Int>) {
    val depths = List(input.toSet().maxOf { it }) { i -> i }
    val costs = depths.map { d -> costOf(d, input) }
    costs.minOf { it }.toString()
}