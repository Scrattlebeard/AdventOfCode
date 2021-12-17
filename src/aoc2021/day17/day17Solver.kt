package aoc2021.day17

import lib.*

suspend fun main() {
    challenge<Pair<Pair<Int, Int>, Pair<Int, Int>>> {

        day(17)

        parser {
            val inp = it.readText()
                .trim()
                .replace("target area: ", "")
                .split(",")
                .map { it.replace("=", "") }
                .map { it.replace("x", "").replace("y", "") }
                .map { it.split("..")}
                .map { it.map {it.trim().toInt()}}
                .map { it[0] to it[1] }
            inp[0] to inp[1]
        }

        solver {
            println("Solving challenge 1...")
            "Todo challenge 1"
        }

        solver {
            println("Solving challenge 2...")
            "Todo challenge 2"
        }
    }
}

fun fireDrone(startX: Int, startY: Int, target: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
    var pos = 0 to 0
    var deltaX = startX
    var deltaY = startY
    val xMin = target.first.first
    val xMax = target.first.second
    val yMin = target.second.first
    val yMax = target.second.second
    while(pos.first <= xMax && deltaX >= 0 && pos.second >= yMin) {

    }
}