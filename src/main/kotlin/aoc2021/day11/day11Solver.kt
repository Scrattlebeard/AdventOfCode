package aoc2021.day11

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<MutableList<Int>>> {
    return setup {
        day(11)
        year(2021)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .get2DArrayOfColumns()
                .map { it.map { it.digitToInt() }.toMutableList() }
        }

        partOne {

            var flashes = 0L
            var state = it

            for (step in 0 until 100) {
                state = state.map { it.map { it + 1 }.toMutableList() }
                flashes += simulateStep(state)
                state.forEachIndexed { i, col -> col.forEachIndexed { j, ele -> if (ele < 0) state[i][j] = 0 } }
            }

            flashes.toString()
        }

        partTwo {

            var state = it
            var step = 1

            while (true) {
                state = state.map { it.map { it + 1 }.toMutableList() }
                simulateStep(state)

                if (state.all { it.all { it < 0 } }) {
                    break
                }

                state.forEachIndexed { i, col -> col.forEachIndexed { j, ele -> if (ele < 0) state[i][j] = 0 } }
                step++
            }

            step.toString()
        }
    }
}

fun simulateStep(grid: List<MutableList<Int>>): Int {
    var flashes = 0
    var flashers = getFlashers(grid)

    while (flashers.isNotEmpty()) {
        simulateFlash(grid, flashers.first())
        flashes++
        flashers = getFlashers(grid)
    }

    return flashes
}

fun getFlashers(grid: List<MutableList<Int>>): List<Pair<Int, Int>> {
    return grid.foldIndexed<MutableList<Int>, List<Pair<Int, Int>>>(listOf()) { i, acc, col ->
        acc.plus(col.mapIndexed { j, ele -> if (ele > 9) i to j else -1 to -1 })
    }.filter { it.first > -1 }
}

fun simulateFlash(grid: List<MutableList<Int>>, point: Pair<Int, Int>) {
    val i = point.first
    val j = point.second
    grid[i][j] = -1
    val neighbors = listOf(
        i + 1 to j,
        i - 1 to j,
        i + 1 to j + 1,
        i + 1 to j - 1,
        i - 1 to j + 1,
        i - 1 to j - 1,
        i to j + 1,
        i to j - 1
    )
    neighbors.forEach { tryIncrement(grid, it) }
}

fun tryIncrement(grid: List<MutableList<Int>>, point: Pair<Int, Int>) {
    if (point.first < grid.size && point.second < grid.first().size && point.first > -1 && point.second > -1 && grid[point.first][point.second] > -1) {
        grid[point.first][point.second] = grid[point.first][point.second] + 1
    }
}