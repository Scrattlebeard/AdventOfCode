package aoc2021.day12

import lib.*
import lib.graph.*

suspend fun main() {
    challenge<Graph<String>> {

        day(12)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .map { it.split("-") }
                .flatMap { listOf(Edge(it[0], it[1]), Edge(it[1], it[0]))}
                .buildGraph()
        }

        solver {
            println("Solving challenge 1...")
            it.countPathsFromNodeTo("start", "end", setOf("start"), setOf(), false).toString()
        }

        solver {
            println("Solving challenge 2...")
            it.countPathsFromNodeTo("start", "end", setOf("start"), setOf(), true).toString()
        }
    }
}

fun Graph<String>.countPathsFromNodeTo(
    node: String,
    dest: String,
    forbiddenNodes: Set<String>,
    visitedNodes: Set<String>,
    allowRepeat: Boolean = true
): Int {
    if (node == dest) {
        return 1
    }

    var newForbiddenNodes = if (!allowRepeat) forbiddenNodes union visitedNodes else forbiddenNodes

    var newAllowRepeat = allowRepeat
    if (allowRepeat && visitedNodes.contains(node)) {
        newForbiddenNodes = forbiddenNodes union visitedNodes
        newAllowRepeat = false
    }
    val newVisitedNodes = if (node.uppercase() == node) visitedNodes else visitedNodes.plus(node)

    val toVisit = this.connections[node]!!.map { it.first }
        .toSet() subtract newForbiddenNodes

    return toVisit.sumOf { countPathsFromNodeTo(it, dest, newForbiddenNodes, newVisitedNodes, newAllowRepeat) }
}