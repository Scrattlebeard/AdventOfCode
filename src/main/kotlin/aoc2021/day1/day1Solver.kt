package aoc2021.day1

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Long>> {
    return setup {
        day(1)
        year(2021)

        parser {
            it.readLines().map(String::toLong)
        }

        partOne {
            println("Solving challenge 1...")
            it.countIncreases().toString()
        }

        partTwo {
            println("Solving challenge 2...")
            it
                .windowed(3)
                .map { it.sum() }
                .countIncreases()
                .toString()
        }
    }
}

fun List<Long>.countIncreases(): Int =
    this
        .windowed(2)
        .count { it[0] < it[1] }
