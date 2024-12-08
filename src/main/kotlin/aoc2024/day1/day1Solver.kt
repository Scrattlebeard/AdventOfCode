package aoc2024.day1

import lib.*
import kotlin.math.abs

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<List<Int>, List<Int>>> {
    return setup {
        day(1)
        year(2024)

        parser {
            var lists = emptyList<Int>() to emptyList<Int>()
            it.readLines()
                .map { it.split("   ") }
                .map { it[0].toInt() to it[1].toInt() }
                .forEach { lists = lists.appendPairwise(it) }
            lists
        }

        partOne {
            it.first
                .sorted()
                .zip(it.second.sorted())
                .sumOf { abs(it.first - it.second) }
                .toString()
        }

        partTwo {
            val counts = it.first.associateWith { v -> it.second.count { it == v } }
            counts.entries.sumOf { it.key * it.value }.toString()
        }
    }
}

fun Pair<List<Int>, List<Int>>.appendPairwise(pair: Pair<Int, Int>): Pair<List<Int>, List<Int>> {
    return (this.first + pair.first to this.second + pair.second)
}