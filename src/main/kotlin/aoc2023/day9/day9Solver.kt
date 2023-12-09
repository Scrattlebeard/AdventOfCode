package aoc2023.day9

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<List<Long>>> {
    return setup {
        day(9)
        year(2023)

        parser {
            it.readLines()
                .map { it.asListOfLongs() }
        }

        partOne {
            it.sumOf { it.predictNext() }
                .toString()
        }

        partTwo {
            it.sumOf { it.asReversed().predictNext() }
                .toString()
        }
    }
}

fun List<Long>.predictNext(): Long {
    val res = mutableListOf<Long>()
    (1 until this.size).forEach {
        res.add(this[it] - this[it - 1])
    }
    return if (res.all { it == 0L }) {
        this.first()
    } else {
        this.last() + res.predictNext()
    }
}