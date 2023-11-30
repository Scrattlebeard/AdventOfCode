package aoc2021.day12

import lib.*
import lib.graph.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<IGraph<String>> {
    return setup {
        day(12)
        year(2021)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .map { it.split("-") }
                .flatMap { listOf(Edge(it[0], it[1]), Edge(it[1], it[0])) }
                .buildGraph()
        }

        partOne {
            it.countPathsFromNodeTo("start", "end", setOf("start"), setOf(), false).toString()
        }

        partTwo {
            it.countPathsFromNodeTo("start", "end", setOf("start"), setOf(), true).toString()
        }
    }
}

fun IGraph<String>.countPathsFromNodeTo(
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

    val toVisit = this.getConnectionsFrom(node)
        .toSet() subtract newForbiddenNodes

    return toVisit.sumOf { countPathsFromNodeTo(it, dest, newForbiddenNodes, newVisitedNodes, newAllowRepeat) }
}