package aoc2021.day19

import lib.*

suspend fun main() {
    challenge<List<List<Triple<Int, Int, Int>>>> {

        day(19)

        parser {
            it.getStringsGroupedByEmptyLine()
                .map { it.subList(1, it.size - 1)}
                .map { it.map { it.split(",").map { it.toInt() } } }
                .map { it.map { Triple(it[0], it[1], it[2]) } }
        }

        solver {
            println("Solving challenge 1...")
            //Use first group as ground truth for coordinate system
            //Sort by each axis, try to match sliding size 12 window?
            "Todo challenge 1"
        }

        solver {
            println("Solving challenge 2...")
            "Todo challenge 2"
        }
    }
}