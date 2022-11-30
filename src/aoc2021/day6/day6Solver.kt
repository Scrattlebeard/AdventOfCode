package aoc2021.day6

import lib.*

suspend fun main() {
    challenge<List<Int>> {

        day(6)
        year(2021)

        parser {
            it.readText()
                .trim()
                .split(',')
                .map { s -> s.toInt() }
        }

        solver {
            println("Solving challenge 1...")
            simulateGrowth(it, 80).toString()
        }

        solver {
            println("Solving challenge 2...")
            simulateGrowth(it, 256).toString()
        }
    }
}

fun simulateGrowth(population: List<Int>, steps: Int): Long {
    var counts = List(9) { i -> population.count { n -> n == i }.toLong() }
    for (step in 0 until steps) {
        counts = counts.mapIndexed { i, _ ->
            when (i) {
                8 -> counts[0]
                6 -> counts[0] + counts[7]
                else -> counts[i + 1]
            }
        }
    }
    return counts.sum()
}