package aoc2023.day6

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

data class BoatRace(val time: Long, val dist: Long)

fun setupChallenge(): Challenge<List<String>> {
    return setup {
        day(6)
        year(2023)

        parser {
            it.readLines()
                .map { it.split(":")[1] }
        }

        partOne {
            val winningStrats= (it[0].asListOfLongs() zip it[1].asListOfLongs())
                .map { BoatRace(it.first, it.second) }
                .map {
                    it.countWinningStrats()
                }

                winningStrats.fold(1) { a, b -> a * b }
                .toString()
        }

        partTwo {
            BoatRace(it[0].replace(" ", "").toLong(), it[1].replace(" ", "").toLong())
                .countWinningStrats()
                .toString()
        }
    }
}

fun BoatRace.countWinningStrats(): Int {
    var speed = 1
    while (speed * (this.time - speed) <= this.dist) {
        speed++
    }
    return (speed..(this.time - speed)).count()
}