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
    fun getConnectionsWithCostFrom(node: T): Set<Pair<T, Long>>
}