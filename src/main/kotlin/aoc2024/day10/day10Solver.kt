package aoc2024.day10

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<List<Int>>> {
    return setup {
        day(10)
        year(2024)

        //input("example.txt")

        parser {
            it.readLines().get2DArrayOfColumns().map { it.map { it.digitToInt() } }
        }

        partOne {
            it.mapIndexed { x, column ->
                column.mapIndexed { y, cell -> if (cell != 0) setOf() else it.reachableTopsFrom(x to y) }
            }
            .sumOf { it.sumOf { it.count() } }
                .toString()
        }

        partTwo {
            it.mapIndexed { x, column ->
                column.mapIndexed { y, cell -> if (cell != 0) 0 else it.pathsToTopFrom(x to y) }
            }
            .sumOf { it.sum() }
                .toString()
        }
    }
}

fun List<List<Int>>.reachableTopsFrom(pos: Pair<Int, Int>): Set<Pair<Int, Int>> {
    val currentHeight = this[pos.x()][pos.y()]
    if (currentHeight == 9) {
        return setOf(pos)
    }

    val options = this.getNeighboursWhere(pos.x(), pos.y()) { it == currentHeight + 1 }
    return options.fold(setOf()) { acc, coord -> acc union this.reachableTopsFrom(coord.first) }
}

fun List<List<Int>>.pathsToTopFrom(pos: Pair<Int, Int>): Int {
    val currentHeight = this[pos.x()][pos.y()]
    if(currentHeight == 9) {
        return 1
    }

    val options = this.getNeighboursWhere(pos.x(), pos.y()) { it == currentHeight + 1 }
    return options.sumOf { this.pathsToTopFrom(it.first) }
}