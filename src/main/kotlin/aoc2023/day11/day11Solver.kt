package aoc2023.day11

import aoc2022.day15.manhattanDistance
import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(11)
        year(2023)

        parser {
            it.readLines().get2DArrayOfColumns()
        }

        partOne {
            val expandedColumns = it.map { it.all { it == '.' } }
            val expandedRows = it.first().indices.map { i -> it.map { it[i] }.all { it == '.' } }
            val galaxies = mutableSetOf<Pair<Int, Int>>()
            it.indices.forEach { i ->
                it[i].indices.forEach { j ->
                    if(it[i][j] == '#') {
                        galaxies.add(i to j)
                    }
                }
            }
            var sum = 0
            galaxies.forEachIndexed{i, g ->
                galaxies.drop(i + 1).forEach {
                    var dist = manhattanDistance(g, it)
                    dist += expandedColumns.subList(minOf(g.x(), it.x()), maxOf(g.x(), it.x())).count { it }
                    dist += expandedRows.subList(minOf(g.y(), it.y()), maxOf(g.y(), it.y())).count { it }
                    sum += dist
                }
            }
            sum.toString()
        }

        partTwo {
            val expandedColumns = it.map { it.all { it == '.' } }
            val expandedRows = it.first().indices.map { i -> it.map { it[i] }.all { it == '.' } }
            val galaxies = mutableSetOf<Pair<Int, Int>>()
            it.indices.forEach { i ->
                it[i].indices.forEach { j ->
                    if(it[i][j] == '#') {
                        galaxies.add(i to j)
                    }
                }
            }
            var sum = 0L
            galaxies.forEachIndexed{i, g ->
                galaxies.drop(i + 1).forEach {
                    var dist = manhattanDistance(g, it).toLong()
                    dist += (expandedColumns.subList(minOf(g.x(), it.x()), maxOf(g.x(), it.x())).count { it } * 999999L)
                    dist += (expandedRows.subList(minOf(g.y(), it.y()), maxOf(g.y(), it.y())).count { it }  * 999999L)
                    sum += dist
                }
            }
            sum.toString()
        }
    }
}