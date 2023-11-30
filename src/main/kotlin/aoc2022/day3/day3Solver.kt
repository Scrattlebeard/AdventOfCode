package aoc2022.day3

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}
fun setupChallenge(): Challenge<List<String>> {
    return setup {
        day(3)
        year(2022)

        parser {
            it.readLines()
        }

        partOne {
            println("Solving challenge 1...")
            it.map { it.substring(0, it.length / 2) to it.substring(it.length / 2) }
                .map { it.first intersect it.second }
                .sumOf { it.first().getPriority() }
                .toString()
        }

        partTwo {
            println("Solving challenge 2...")
            it.windowed(3, 3)
                .map { it[0] intersect it[1] intersect it[2] }
                .sumOf { it.first().getPriority() }
                .toString()
        }
    }
}

fun Char.getPriority(): Int {
    return if (this.isUpperCase()) this.code - (65 - 27) else this.code - 96
}