package aoc2022.day15

import lib.*
import kotlin.math.absoluteValue

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Pair<Pair<Int, Int>, Pair<Int, Int>>>> {
    return setup {
        day(15)
        year(2022)

        //input("example.txt")

        parser {
            it.readLines()
                .map {
                    it.replace("Sensor at ", "")
                        .replace(" closest beacon is at ", "")
                        .split(":")
                }
                .map { it.map { it.split(", ") } }
                .map {
                    (it[0][0].replace("x=", "").toInt() to it[0][1].replace("y=", "")
                        .toInt()) to (it[1][0].replace("x=", "").toInt() to it[1][1].replace("y=", "").toInt())
                }
        }

        partOne {
            val row = if (it.isExample()) 2000000 else 10
            val blockingSensorsWithRange =
                it.map { it.first to (manhattanDistance(it.first, it.second) - (it.first.second - row).absoluteValue) }.filter { it.second >= 0 }
            val blockedColumns = setOf<Int>().toMutableSet()
            blockingSensorsWithRange.forEach { blockedColumns.addAll((it.first.first - it.second)..(it.first.first + it.second)) }
            val beaconsInRow = it.filter { it.second.second == row }.distinctBy { it.second }
            (blockedColumns.size - beaconsInRow.size).toString()
        }

        partTwo {
            val coverage = it.map { it.first to manhattanDistance(it.first, it.second) }
            val xTuning = 4000000L
            val limit = if (it.isExample()) xTuning else 20
            val (x, y) = findUncoveredCoordinate(coverage, limit)!!

            (x * xTuning + y).toString()
        }
    }
}

fun List<Pair<Pair<Int, Int>, Pair<Int, Int>>>.isExample(): Boolean {
    return this.maxOf { it.first.second; it.second.second } > 200000
}

fun manhattanDistance(from: Pair<Int, Int>, to: Pair<Int, Int>): Int {
    return (from.first - to.first).absoluteValue + (from.second - to.second).absoluteValue
}

fun findUncoveredCoordinate(coverage: List<Pair<Pair<Int, Int>, Int>>, limit: Long): Pair<Int, Int>? {
    var (x, y) = 0 to 0
    while (x < limit) {
        while (y < limit) {
            val blocker = coverage.firstOrNull { it.second >= manhattanDistance(x to y, it.first) }
            if (blocker == null) {
                return x to y
            } else {
                y = (blocker.first.second + (blocker.second - (blocker.first.first - x).absoluteValue)) + 1
            }
        }
        y = 0
        x++
    }
    return null
}