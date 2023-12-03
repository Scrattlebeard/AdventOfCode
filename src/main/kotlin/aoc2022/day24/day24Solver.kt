package aoc2022.day24

import lib.*
import lib.graph.IGraph
import lib.graph.UnitValueGraph

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<String>>> {
    return setup {
        day(24)
        year(2022)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()

                //Prune borders
                .drop(1)
                .dropLast(1)
                .map { it.substring(1, it.length - 1) }

                .get2DArrayOfColumns()
                .map { it.map { it.toString() }.map { if (it == ".") "" else it }.toTypedArray() }.toTypedArray()
        }

        partOne {

            val mapsByTime = getMapsByTime(it)
            val graph = buildDynamicGraph(mapsByTime)

            val dest = it.size - 1 to it.first().size

            val res =
                graph.shortestDistanceFromTo(Triple(0, -1, 0)) { it.first == dest.x() && it.second == dest.y() }
            res.toString()
        }

        partTwo {
            val mapsByTime = getMapsByTime(it)
            val graph = buildDynamicGraph(mapsByTime)

            val dest = it.size - 1 to it.first().size
            val iterations = mapsByTime.size

            val res1 = graph.shortestDistanceFromTo(
                Triple(
                    0,
                    -1,
                    0
                )
            ) { it.first == dest.x() && it.second == dest.y() }!!
            println("Part 1: $res1")

            val res2 = graph.shortestDistanceFromTo(
                Triple(
                    dest.x(),
                    dest.y(),
                    res1.toInt() % iterations
                )
            ) { it.first == 0 && it.second == -1 }
            println("Part 2: $res2")

            val res3 = graph.shortestDistanceFromTo(
                Triple(
                    0,
                    -1,
                    (res1 + res2!!).toInt() % iterations
                )
            ) { it.first == dest.x() && it.second == dest.y() }!!
            println("Part 3: $res3")

            (res1 + res2 + res3).toString()
        }
    }
}

fun Array<Array<String>>.moveBlizzards(): Array<Array<String>> {
    val xMax = this.size
    val yMax = this.first().size
    val next = Array(xMax) { Array(yMax) { "" } }
    this.forEachIndexed { x, cols ->
        cols.forEachIndexed { y, cell ->
            cell.forEach {
                val dest = when (it) {
                    '^' -> x to (y - 1).wrapAt(yMax)
                    'v' -> x to (y + 1).wrapAt(yMax)
                    '<' -> (x - 1).wrapAt(xMax) to y
                    '>' -> (x + 1).wrapAt(xMax) to y
                    else -> throw Exception("$it is not a blizzard")
                }
                next[dest.x()][dest.y()] = next[dest.x()][dest.y()] + it
            }
        }
    }
    return next
}

fun getMapsByTime(startingMap: Array<Array<String>>): Map<Int, Array<Array<String>>> {
    val mapsByFingerprint = mutableMapOf(startingMap.fingerprint() to (startingMap to 0))
    var done = false
    var map = startingMap
    var i = 0
    while (!done) {
        map = map.moveBlizzards()
        i++
        val fingerprint = map.fingerprint()
        if (mapsByFingerprint.containsKey(fingerprint)) {
            done = true
        } else {
            mapsByFingerprint[fingerprint] = map to i
        }
    }
    println("Computed all maps with i = $i")

    return mapsByFingerprint.map { it.value.second to it.value.first }.toMap()
}

fun getNodes(xMax: Int, yMax: Int, numIterations: Int): Set<Triple<Int, Int, Int>> {
    val iterations = 0 until numIterations
    val baseNodes = (0..xMax)
        .flatMap { x ->
            (0..yMax)
                .flatMap { y ->
                    iterations
                        .map { Triple(x, y, it) }
                }
        }.toSet()

    val startNodes = iterations.map { Triple(0, -1, it) }
    val endNodes = iterations.map { Triple(xMax, yMax + 1, it) }

    return baseNodes + startNodes + endNodes
}

fun getConnections(
    node: Triple<Int, Int, Int>,
    mapsByTime: Map<Int, Array<Array<String>>>
): Set<Triple<Int, Int, Int>> {
    val currentMap = mapsByTime[(node.third + 1) % mapsByTime.size]!!
    val nextTurn = node.third + 1 % currentMap.size

    val neighbours =
        if(node.second == -1 || node.second == currentMap.first().size || currentMap[node.first][node.second].isEmpty())
            //Add waiting option
            mutableSetOf(Triple(node.first, node.second, nextTurn))
        else
            mutableSetOf()

    neighbours.addAll(currentMap.getNeighbours(node.first, node.second)
        .filter { it.second.isEmpty() }
        .map { Triple(it.first.first, it.first.second, nextTurn) } )

    if (node.first == 0 && node.second == 0)
        neighbours += Triple(0, -1, nextTurn)

    if (node.first == currentMap.size - 1 && node.second == currentMap.first().size - 1)
        neighbours += Triple(currentMap.size - 1, currentMap.first().size, nextTurn)

    return neighbours
}

fun buildDynamicGraph(mapsByTime: Map<Int, Array<Array<String>>>): IGraph<Triple<Int, Int, Int>> {
    val aMap = mapsByTime[0]!!
    return UnitValueGraph(
        { getNodes(aMap.size - 1, aMap.first().size - 1, mapsByTime.size) },
        { n -> getConnections(n, mapsByTime) })
}

fun Array<Array<String>>.fingerprint(): String {
    return this.joinToString { it.joinToString { it } }
}