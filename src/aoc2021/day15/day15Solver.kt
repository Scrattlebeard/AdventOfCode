package aoc2021.day15

import lib.*
import lib.graph.*

suspend fun main() {
    challenge<Pair<Graph<String>, String>> {

        day(15)

        parser {
            val square = it.readLines().get2DArray()
            val edges = mutableListOf<Edge<String>>()
            square.forEachIndexed { i, l ->
                l.forEachIndexed { j, _ ->
                    square.getNeighbours(i, j).forEach {
                        edges.add(
                            Edge(
                                "$i$j",
                                "${it.first.first}${it.first.second}",
                                it.second.digitToInt()
                            )
                        )
                    }
                }
            }
            Graph(edges) to "${square.size - 1}${square.first().size - 1}"
        }

        solver {
            println("Solving challenge 1...")
            it.first.shortestPathFromNodeTo("00", it.second).toString()
        }

        solver {
            println("Solving challenge 2...")
            "Todo challenge 2"
        }
    }
}

fun Graph<String>.shortestPathFromNodeTo(
    node: String,
    dest: String,
): Long {

    val unvisitedNodes = this.nodes.associateWith { Long.MAX_VALUE }.toMutableMap()
    unvisitedNodes[node] = 0

    var currentNode: String? = node
    while (currentNode != null) {
        if (currentNode == dest) {
            return unvisitedNodes[currentNode]!!
        }

        this.connections[currentNode]!!
            .filter { unvisitedNodes.containsKey(it.first) }
            .forEach {
                unvisitedNodes[it.first] =
                    minOf(
                        unvisitedNodes.getOrDefault(it.first, Long.MAX_VALUE),
                        unvisitedNodes[currentNode]!! + it.second
                    )
            }
        unvisitedNodes.remove(currentNode)
        currentNode = unvisitedNodes.minByOrNull { it.value }?.key
    }

    return Long.MAX_VALUE
}