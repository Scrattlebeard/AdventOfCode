package aoc2021.day9

import lib.*

suspend fun main() {
    challenge<List<List<Int>>> {

        day(9)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .get2DArray()
                .map { it.map { it.digitToInt() } }
        }

        solver {
            println("Solving challenge 1...")
            it.mapIndexed { i, l ->
                l.mapIndexed { j, num ->
                    if (isLowPoint(i, j, it)) num + 1 else 0
                }
            }
                //.map { it.filter { it > 0 } }
                .sumOf { it.sum() }
                .toString()
        }

        solver {
            println("Solving challenge 2...")
            "Todo challenge 2"
        }
    }
}

fun isLowPoint(i: Int, j: Int, map: List<List<Int>>): Boolean {
    val value = map[i][j]
    val neighbors = listOf(map.tryGet(i - 1, j), map.tryGet(i + 1, j), map.tryGet(i, j - 1), map.tryGet(i, j + 1))
    return neighbors.all { it > value }
}

fun List<List<Int>>.tryGet(i: Int, j: Int): Int {
    return if (i < this.size && j < this.first().size && i > -1 && j > -1) {
        this[i][j]
    } else Int.MAX_VALUE
}