package aoc2021.day15

import lib.*
import lib.graph.*
import java.util.*

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

    val toVisit = PriorityQueue<Pair<Pair<Int, Int>, Long>> { a, b -> a.second.compareTo(b.second) }
    val visitTracker = mutableMapOf(start to 0L)
    var current: Pair<Pair<Int, Int>, Long>? = start to 0
    while (current != null) {
        val currentPos = current.first
        if (currentPos == dest) {
            return current.second
        }
        visitTracker[currentPos] = current.second

        this.getNeighbours(currentPos.first, currentPos.second)
            .filter { visitTracker.getOrDefault(it.first, -1) == -1L }
            .forEach { neighbour ->
                val match = toVisit.firstOrNull { it.first == neighbour.first }
                val newDist = visitTracker[currentPos]!! + neighbour.second
                if (newDist < (match?.second ?: Long.MAX_VALUE)) {
                    toVisit.remove(match)
                    toVisit.add(neighbour.first to newDist)
                }
            }

        current = toVisit.poll()
    }
    return Long.MAX_VALUE
}