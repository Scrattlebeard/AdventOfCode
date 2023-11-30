package aoc2022.day1

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Int>> {
    return setup {

        day(1)
        year(2022)

        parser {
            it.getStringsGroupedByEmptyLine()
                .map { it.sumOf { it.toInt() } }
        }

        partOne {
            println("Solving challenge 1...")
            it.max()
                .toString()
        }

        partTwo {
            println("Solving challenge 2...")
            it.sortedDescending()
                .take(3)
                .sum()
                .toString()
        }
    }
}