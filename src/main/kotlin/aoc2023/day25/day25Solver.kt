package aoc2023.day25

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<String>> {
    return setup {
        day(25)
        year(2023)

        parser {
            it.readLines()
        }

        partOne {
            null
        }

        partTwo {
            null
        }
    }
}