package lib.graph

interface IGraph<T> {

    var distances: Map<T, Map<T, Long?>>?

    fun shortestDistanceFromTo(node: T, dest: T): Long? {
        return shortestDistanceFromTo(node) { it == dest }
    }

    fun computeDistances(): Map<T, Map<T, Long?>> {
        if (distances == null) {
            val nodes = getNodes()
            distances = nodes.associateWith { n -> nodes.associateWith { shortestDistanceFromTo(n, it) } }
        }
        return distances!!
    }

    fun shortestDistanceFromTo(node: T, predicate: (T) -> Boolean): Long?
    fun getNodes(): Set<T>
    fun getConnectionsFrom(node: T): Set<T>
}

class Graph<T>(val connections: Map<T, List<Pair<T, Long>>>) : IGraph<T> {

    override var distances: Map<T, Map<T, Long?>>? = null
    override fun getNodes(): Set<T> {
        return connections.keys
    }

    override fun getConnectionsFrom(node: T): Set<T> {
        return connections[node]!!.map { it.first }.toSet()
    }

    override fun shortestDistanceFromTo(
        node: T,
        predicate: (T) -> Boolean,
    ): Long? {

        val visitableNodes = mutableMapOf<T, Long>()
        //val unvisitedNodes = this.nodes.associateWith<T, Long?> { null }.toMutableMap()
        //unvisitedNodes[node] = 0
        visitableNodes[node] = 0

        var currentNode: T? = node
        while (currentNode != null) {
            if (predicate(currentNode)) {
                return visitableNodes[currentNode]
            }

            this.connections[currentNode].orEmpty()
                //.filter { unvisitedNodes.containsKey(it.first) }
                .forEach {
                    val oldDist = visitableNodes[it.first]
                    val newDist = visitableNodes[currentNode]!! + it.second

                    if (oldDist == null || newDist < oldDist) {
                        //unvisitedNodes[it.first] = newDist
                        visitableNodes[it.first] = newDist
                    }
                }

            //unvisitedNodes.remove(currentNode)
            //unvisitedNodes[currentNode] = null
            visitableNodes.remove(currentNode)
            currentNode = visitableNodes.minByOrNull { it.value }?.key
        }

        return null
    }
}

class UnitValueGraph<T>(val nodeFunction: () -> Set<T>, val edgeFunction: (T) -> Set<T>) : IGraph<T> {
    constructor(connections: Map<T, Set<T>>) : this({ connections.keys }, { key -> connections[key].orEmpty() })

    override var distances: Map<T, Map<T, Long?>>? = null

    //Todo: Make Lazy?
    override fun getNodes(): Set<T> {
        return nodeFunction()
    }

    override fun getConnectionsFrom(node: T): Set<T> {
        return edgeFunction(node)
    }

    override fun shortestDistanceFromTo(
        node: T,
        predicate: (T) -> Boolean,
    ): Long? {

        val visitableNodes = mutableMapOf<T, Long>()
        visitableNodes[node] = 0

        var currentNode: T? = node
        while (currentNode != null) {
            if (predicate(currentNode)) {
                return visitableNodes[currentNode]
            }

            getConnectionsFrom(currentNode)
                .forEach {
                    val oldDist = visitableNodes[it]
                    val newDist = visitableNodes[currentNode]!! + 1

                    if (oldDist == null || newDist < oldDist) {
                        visitableNodes[it] = newDist
                    }
                }

            visitableNodes.remove(currentNode)
            currentNode = visitableNodes.minByOrNull { it.value }?.key
        }

        return null
    }
}

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

