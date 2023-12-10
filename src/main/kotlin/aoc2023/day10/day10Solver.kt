package aoc2023.day10

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(10)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines()
                .get2DArrayOfColumns()
        }

        partOne {
            val start = it.getCoordinatesOf('S')!!
            val loop = buildLoop(it, start)
            (loop.size / 2 + loop.size % 2).toString()
        }

        partTwo {
            val start = it.getCoordinatesOf('S')!!
            val loop = buildLoop(it, start).toList()
            val xMax = loop.maxOf { it.x() }

            val innerPoints = mutableSetOf<Pair<Int, Int>>()
            val startIndex = loop.indexOfFirst { it.x() == xMax }
            var direction = loop[startIndex].cardinalDirectionTo(loop[(startIndex + 1) % loop.size])
            val inside = relativeDirectionTo(direction, CardinalDirection.West)
            loop.indices
                .map { (it + startIndex) % loop.size }
                .forEach {
                    val current = loop[it]
                    val candidates = mutableSetOf(current.adjacent(direction.turn(inside)))
                    direction = current.cardinalDirectionTo(loop[(it + 1) % loop.size])
                    candidates.add(current.adjacent(direction.turn(inside)))
                    innerPoints.addAll(candidates.filter { it !in loop })
            }

            val toExpand = innerPoints.toMutableSet()
            while(toExpand.isNotEmpty()) {
                val current = toExpand.first()
                toExpand.remove(current)
                val candidates = current.getNeighbours(true).filter { it !in loop && it !in innerPoints }
                candidates.forEach {
                    toExpand.add(it)
                    innerPoints.add(it)
                }
            }

            innerPoints.size.toString()
        }
    }
}

fun Array<Array<Char>>.getCoordinatesOf(c: Char): Pair<Int, Int>? {
    this.indices.forEach { i ->
        val column = this[i]
        column.indices.forEach { j ->
            if (column[j] == c) {
                return i to j
            }
        }
    }
    return null
}

fun Char.getConnections(): List<CardinalDirection> {
    return when (this) {
        '|' -> listOf(CardinalDirection.North, CardinalDirection.South)
        '-' -> listOf(CardinalDirection.East, CardinalDirection.West)
        'L' -> listOf(CardinalDirection.North, CardinalDirection.East)
        'J' -> listOf(CardinalDirection.North, CardinalDirection.West)
        '7' -> listOf(CardinalDirection.South, CardinalDirection.West)
        'F' -> listOf(CardinalDirection.South, CardinalDirection.East)
        else -> listOf()
    }
}

fun buildLoop(map: Array<Array<Char>>, start: Pair<Int, Int>): Set<Pair<Int, Int>> {

    var currentDirection = CardinalDirection.values().first { dir ->
        val neighbour = start.adjacent(dir)
        map.isValidCoord(neighbour.x(), neighbour.y())
                && map[neighbour.x()][neighbour.y()].getConnections().contains(dir.opposite())
    }

    var current = start.adjacent(currentDirection)
    val loop = mutableSetOf(start, current)
    var i = 0
    do {
        currentDirection = map[current.x()][current.y()].getConnections().first { it != currentDirection.opposite() }
        current = current.adjacent(currentDirection)
        loop.add(current)
        i++
    } while (current != start)

    return loop
}