package aoc2015.day2

import lib.*

suspend fun main() {
    challenge<List<Triple<Int, Int, Int>>> {

        day(2)
        year(2015)

        parser {
            it.readLines()
                .map { it.split('x') }
                .map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
        }

        solver {
            println("Solving challenge 1...")
            it.map { Triple(it.first * it.second, it.second * it.third, it.third * it.first) }
                .map { it.toList().min() + 2 * it.first + 2 * it.second + 2 * it.third }
                .sum()
                .toString()
        }

        solver {
            println("Solving challenge 2...")
            it.map { Pair<Int, List<Int>>(it.first * it.second * it.third, it.toList().sorted())  }
                .map { it.first + 2 * it.second[0] + 2 * it.second[1] }
                .sum()
                .toString()
        }
    }
}