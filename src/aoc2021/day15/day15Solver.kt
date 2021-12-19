package aoc2021.day15

import lib.*
import lib.graph.*

suspend fun main() {
    challenge<Pair<List<List<Int>>, Pair<Int, Int>>> {

        day(15)

        input("example.txt")
        input("input.txt")

        parser {
            val square = it.readLines().get2DArray()
                .map { it.map { it.digitToInt() } }
            square to (square.size - 1 to square.first().size - 1)
        }

        solver {
            println("Solving challenge 1...")
            it.first.shortestPathFromTo(0 to 0, it.second).toString()
        }

        solver {
            println("Solving challenge 2...")
            val square = it.first
            val x = square.size
            val y = square.first().size
            val cave = List(5 * x) { i ->
                List(5 * y) { j ->
                    val value = square[i % x][j % y] + i / x + j / y
                    if (value > 9) (value % 10) + 1 else value
                }
            }
            cave.shortestPathFromTo(0 to 0, it.second.first * 5 + 4 to it.second.second * 5 + 4).toString()
        }
    }
}

fun List<List<Int>>.shortestPathFromTo(start: Pair<Int, Int>, dest: Pair<Int, Int>): Long {

    val unvisitedDistances = mutableMapOf<Pair<Int, Int>, Long>()
    unvisitedDistances[start] = 0
    var currentPos: Pair<Int, Int>? = start
    while (currentPos != null) {
        if (currentPos == dest) {
            return unvisitedDistances[currentPos]!!
        }

        this.getNeighbours(currentPos.first, currentPos.second)
            .filter { unvisitedDistances.getOrDefault(it.first, Long.MAX_VALUE) > 0 }
            .forEach {
                unvisitedDistances[it.first] = minOf(
                    unvisitedDistances[currentPos]!! + it.second,
                    unvisitedDistances.getOrDefault(it.first, Long.MAX_VALUE)
                )
            }

        unvisitedDistances[currentPos] = -1
        currentPos = unvisitedDistances.filter { it.value > 0 }.minByOrNull { it.value }?.key
    }

    return Long.MAX_VALUE
}