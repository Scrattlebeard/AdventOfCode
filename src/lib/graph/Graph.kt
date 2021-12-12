package lib.graph

class Graph<T>(val edges: List<Edge<T>>) {
    val nodes = (edges.map { it.start } union edges.map { it.end }).toSet()
    val connections = nodes.associateWith { node ->
        edges
            .filter { it.start == node || it.end == node }
            .map { if (it.start == node) it.end to it.cost else it.start to it.cost }
    }
}

class Edge<T>(val start: T, val end: T, val cost: Int = 1)

fun <T> List<Edge<T>>.buildGraph(): Graph<T> {
    return Graph(this)
}