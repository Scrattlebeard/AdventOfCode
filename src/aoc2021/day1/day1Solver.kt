package aoc2021.day1

import lib.*

suspend fun main() {
    challenge<List<Long>> {

        day(1)

        parser {
            it.readLines().map(String::toLong)
        }

        solver {
            println("Solving challenge 1...")
            it.countIncreases(1).toString()
        }

        solver {
            println("Solving challenge 2...")
            it.countIncreases(3).toString()
        }
    }
}

fun List<Long>.countIncreases(windowSize: Int): Int {
    return this.windowed(windowSize)
        .map { nums -> nums.sum() }
        .windowed(2)
        .map { sums -> sums[0] < sums[1] }
        .count { it }
}