package aoc2015.day1

import lib.*

suspend fun main() {
    challenge<String> {

        day(1)
        year(2015)

        parser {
            it.readText()
        }

        solver {
            println("Solving challenge 1...")
            it.sumOf { if (it == '(') 1 else -1 as Int }
                .toString()
        }

        solver {
            println("Solving challenge 2...")
            var floor = 0
            val path = it.map { if (it == '(') 1 else -1 }
                .takeWhile { floor += it; floor >= 0 }
            (path.count() + 1).toString()
        }
    }
}