package aoc2023.day17

import lib.*
import lib.graph.Graph

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Int>>> {
    return setup {
        day(17)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines().get2DArrayOfColumns().map { it.map { it.digitToInt() }.toTypedArray() }.toTypedArray()
        }

        partOne {
            val costs = it
            val nodes =
                it.indices.flatMap { i -> it[i].indices.flatMap { j -> CardinalDirection.entries.map { (i to j) to it } } }
            val connections =
                nodes.associateWith { costs.getConnectionsFrom(it).map { (it.destination to it.direction) to it.cost } }
                    .toMutableMap()
            val start = (-1 to -1) to CardinalDirection.East
            val startConnections = costs.getMoves(
                (0 to 0) to CardinalDirection.East,
                setOf(CardinalDirection.East, CardinalDirection.South),
                1..3
            )
            connections[start] = startConnections.map { (it.destination to it.direction) to it.cost }
            val dest = (costs.size - 1) to (costs.first().size - 1)
            val graph = Graph({nodes.toSet() + start}, {node -> (if(node.first.first == -1) startConnections else costs.getConnectionsFrom(node)).map { (it.destination to it.direction) to it.cost }.toSet()})
            val bound = costs.getUpperBound(1..3)
            graph.limit = bound
            graph.shortestDistanceFromTo(start) { it.first == dest }.toString()
        }

        partTwo {
            val costs = it
            val nodes =
                it.indices.flatMap { i -> it[i].indices.flatMap { j -> CardinalDirection.entries.map { (i to j) to it } } }
            val connections = nodes.associateWith {
                costs.getConnectionsFrom(it, 4..10).map { (it.destination to it.direction) to it.cost }
            }.mapKeys { "${it.key.first}-${it.key.second}" }.mapValues { it.value.map { "${it.first.first}-${it.first.second}" to it.second } }.toMutableMap()
            val startConnections = costs.getMoves(
                (0 to 0) to CardinalDirection.East,
                setOf(CardinalDirection.East, CardinalDirection.South),
                4..10
            )
            connections["start"] = startConnections.map { "${it.destination}-${it.direction}" to it.cost }
            val graph = Graph(connections)
            graph.shortestDistanceFromTo("start") {
                it.startsWith("${costs.size - 1 to costs.first().size - 1}")
            }.toString()
        }
    }
}

data class Move(val destination: Pair<Int, Int>, val direction: CardinalDirection, val cost: Long)

fun Array<Array<Int>>.getConnectionsFrom(
    source: Pair<Pair<Int, Int>, CardinalDirection>,
    allowedDistance: IntRange = 1..3
): List<Move> {
    val directions = setOf(source.second.turn(RelativeDirection.Left), source.second.turn(RelativeDirection.Right))
    return this.getMoves(source, directions, allowedDistance)
}

fun Array<Array<Int>>.getMoves(
    source: Pair<Pair<Int, Int>, CardinalDirection>,
    directions: Set<CardinalDirection>,
    allowedDistance: IntRange
): List<Move> {
    val moves = mutableListOf<Move>()
    directions.forEach {
        var i = 1
        var current = source.first.adjacent(it)
        var cost = 0L
        while (i <= allowedDistance.last && isValidCoord(current)) {
            cost += this[current.first][current.second]
            if (i in allowedDistance) {
                moves.add(Move(current, it, cost))
            }
            current = current.adjacent(it)
            i++
        }
    }
    return moves
}

fun Array<Array<Int>>.getUpperBound(validMoves: IntRange): Long {
    val dest = this.size - 1 to this.first().size - 1
    var current = 0 to 0
    var res = 0L

    while(current != dest) {
        val toMoveX = minOf(dest.first - current.first, validMoves.last)
        val toMoveY = minOf(dest.second - current.second, validMoves.last)
        repeat(toMoveX) {
            current = current.adjacent(CardinalDirection.East)
            res += this[current.first][current.second]
        }
        repeat(toMoveY) {
            current = current.adjacent(CardinalDirection.South)
            res += this[current.first][current.second]
        }
        if(toMoveX + toMoveY == 0 || !isValidCoord(current)) {
            throw Exception("Error navigating to dest")
        }
    }
    return res
}