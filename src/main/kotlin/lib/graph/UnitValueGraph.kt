package lib.graph

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

    override fun getConnectionsWithCostFrom(node: T): Set<Pair<T, Long>> {
        return getConnectionsFrom(node).map { it to 1L }.toSet()
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