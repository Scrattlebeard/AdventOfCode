package aoc2021.day13

import lib.*

suspend fun main() {
    challenge<Pair<List<List<Boolean>>, List<Pair<String, Int>>>> {

        day(13)

        input("example.txt")
        input("input.txt")

        parser {
            val inp = it.getStringsGroupedByEmptyLine()

            //Paper
            val marks = inp[0].map { it.split(",").map { it.toInt() } }.map { it[0] to it[1] }
            val width = marks.maxOf { it.first } + 1
            val height = marks.maxOf { it.second } + 1
            val paper = List(width) { MutableList(height) { false } }
            marks.forEach { paper[it.first][it.second] = true }

            //Instructions
            val inst = inp[1].map {
                it.replace("fold along ", "")
                    .split("=")
            }
                .map { it[0] to it[1].toInt() }

            paper to inst
        }

        solver {
            println("Solving challenge 1...")
            it.first.foldAlongAxis(it.second.first())
                .fold(0) { acc, l -> acc + l.count { it } }
                .toString()
        }

        solver {
            println("Solving challenge 2...")
            it.second
                .fold(it.first) { paper, inst -> paper.foldAlongAxis(inst) }
                .getRepr()
        }
    }
}

fun List<List<Boolean>>.foldAlongAxis(inst: Pair<String, Int>): List<List<Boolean>> {
    return when (inst.first) {
        "x" -> foldAlongX(inst.second)
        "y" -> foldAlongY(inst.second)
        else -> throw Exception("Unknown axis " + inst.first)
    }
}

fun List<List<Boolean>>.foldAlongX(coord: Int): List<List<Boolean>> {
    val res = List(coord) { List(this.first().size) { false } }
    return res.mapIndexed { x, r -> r.mapIndexed { y, _ -> this[x][y] || this.tryGet(coord + (coord - x), y) } }
}

fun List<List<Boolean>>.foldAlongY(coord: Int): List<List<Boolean>> {
    val res = List(this.size) { List(coord) { false } }
    return res.mapIndexed { x, r -> r.mapIndexed { y, _ -> this[x][y] || this.tryGet(x, coord + (coord - y)) } }
}

fun List<List<Boolean>>.getRepr(): String {
    val output = StringBuilder()
    for (j in 0 until this.first().size) {
        for (i in 0 until this.size) {
            output.append(if (this[i][j]) "#" else ".")
        }
        output.append("\n")
    }
    return "\n$output"
}

fun List<List<Boolean>>.tryGet(x: Int, y: Int): Boolean {
    if (x >= this.size || y >= this.first().size) {
        return false
    }
    return this[x][y]
}