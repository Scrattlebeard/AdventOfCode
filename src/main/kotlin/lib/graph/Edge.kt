package lib.graph

class Edge<T>(val start: T, val end: T, val cost: Long = 1)

fun <T> List<Edge<T>>.buildGraph(): IGraph<T> {
    val nodeSet = (this.map { it.start } union this.map { it.end }).toSet()
    return if (this.all { it.cost == 1L }) {
        val connections = nodeSet.associateWith { node -> this.filter { it.start == node }.map { it.end }.toSet() }
        UnitValueGraph(connections)
    } else {
        val connectionMap = nodeSet.associateWith { node ->
            this
                .filter { it.start == node }
                .map { it.end to it.cost }
        }
        Graph(connectionMap)
    }
}