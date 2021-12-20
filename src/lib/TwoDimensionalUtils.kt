package lib

fun getNeighbourCoords(i: Int, j: Int, diagonals: Boolean = false): List<Pair<Int, Int>> {
    var neighbours = listOf(i to j + 1, i to j - 1, i + 1 to j, i - 1 to j)
    if (diagonals) {
        neighbours = neighbours.plus(listOf(i + 1 to j + 1, i + 1 to j - 1, i - 1 to j + 1, i - 1 to j - 1))
    }
    return neighbours
}

fun <T> Array<Array<T>>.getNeighbours(i: Int, j: Int, diagonals: Boolean = false): List<Pair<Pair<Int, Int>, T>> {
    val neighbours = getNeighbourCoords(i, j, diagonals)
    return neighbours.filter { this.isValidCoord(it.first, it.second) }.map { it to this[it.first][it.second] }
}

fun <T> Array<Array<T>>.isValidCoord(i: Int, j: Int): Boolean {
    return i > -1 && j > -1 && i < this.size && j < this.first().size
}

fun <T> Array<Array<T>>.print() {
    for(i in this.first().indices) {
        for(j in this.indices) {
            print(this[j][i])
        }
        println()
    }
}

fun <T> List<List<T>>.getNeighbours(i: Int, j: Int): List<Pair<Pair<Int, Int>, T>> {
    val neighbours =
        listOf(i to j + 1, i to j - 1, i + 1 to j, i - 1 to j).filter { this.isValidCoord(it.first, it.second) }
    return neighbours.map { it to this[it.first][it.second] }
}

fun <T> Collection<Collection<T>>.isValidCoord(i: Int, j: Int): Boolean {
    return i > -1 && j > -1 && i < this.size && j < this.first().size
}