package lib.graph

class Graph<T>(val nodeFunction: () -> Set<T>, val edgeFunction: (T) -> Set<Pair<T, Long>>) : IGraph<T> {
    constructor(connections: Map<T, List<Pair<T, Long>>>) : this(
        { connections.keys },
        { key -> connections[key].orEmpty().toSet() })

    override var distances: Map<T, Map<T, Long?>>? = null

    var limit: Long? = null

    override fun getNodes(): Set<T> {
        return nodeFunction()
    }

    override fun getConnectionsFrom(node: T): Set<T> {
        return getConnectionsWithCostFrom(node).map { it.first }.toSet()
    }

    override fun getConnectionsWithCostFrom(node: T): Set<Pair<T, Long>> {
        return edgeFunction(node)
    }

    override fun shortestDistanceFromTo(
        node: T,
        predicate: (T) -> Boolean,
    ): Long? {

        val visitableNodes = mutableMapOf<T, Long>()
        visitableNodes[node] = 0L

        var currentNode: T? = node
        while (currentNode != null) {
            if (predicate(currentNode)) {
                return visitableNodes[currentNode]
            }

            getConnectionsWithCostFrom(currentNode)
                .forEach {
                    val oldDist = visitableNodes[it.first]
                    val newDist = visitableNodes[currentNode]!! + it.second

                    if (limit == null || newDist < limit!!) {
                        if (oldDist == null || newDist < oldDist) {
                            visitableNodes[it.first] = newDist
                        }
                    }
                }

            visitableNodes.remove(currentNode)
            currentNode = visitableNodes.minByOrNull { it.value }?.key
        }

        return null
    }
}


