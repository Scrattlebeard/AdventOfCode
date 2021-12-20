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
            val lowPoints = it.mapIndexed { i, l ->
                l.mapIndexed { j, _ ->
                    if (isLowPoint(i, j, it)) i to j else -1 to -1
                }
            }
                .fold(listOf<Pair<Int, Int>>()) { acc, l -> acc.plus(l) }
                .associateWith { 0 }
                .toMutableMap()

            it.mapIndexed { i, l -> l.mapIndexed { j, _ -> findLowPoint(i to j, it, lowPoints) } }
                .forEach {
                    it.forEach {
                        lowPoints[it] = lowPoints[it]!! + 1
                    }
                }

            var sortedPoints = lowPoints.entries.filter { it.key.first != -1 }
                .sortedByDescending { it.value }
                .subList(0, 3)

            var res = sortedPoints.fold(1) { acc, n -> acc * n.value }
                .toString()

            res
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

fun findLowPoint(point: Pair<Int, Int>, map: List<List<Int>>, lowPoints: Map<Pair<Int, Int>, Int>): Pair<Int, Int> {
    val i = point.first
    val j = point.second
    if (map[i][j] == 9) {
        return -1 to -1
    }

    if (lowPoints.keys.contains(point)) {
        return point
    }

    val minN = listOf(
        (i - 1 to j) to map.tryGet(i - 1, j),
        (i + 1 to j) to map.tryGet(i + 1, j),
        (i to j - 1) to map.tryGet(i, j - 1),
        (i to j + 1) to map.tryGet(i, j + 1)
    ).minByOrNull { p -> p.second }

    return findLowPoint(minN!!.first, map, lowPoints)
}