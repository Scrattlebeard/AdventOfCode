package lib

fun getNeighbourCoords(i: Int, j: Int, diagonals: Boolean = false): List<Pair<Int, Int>> {
    var neighbours = listOf(i to j + 1, i to j - 1, i + 1 to j, i - 1 to j)
    if (diagonals) {
        neighbours = neighbours.plus(listOf(i + 1 to j + 1, i + 1 to j - 1, i - 1 to j + 1, i - 1 to j - 1))
    }
    return neighbours
}

fun Pair<Int, Int>.getNeighbours(diagonals: Boolean = false): List<Pair<Int, Int>> {
    return getNeighbourCoords(this.first, this.second, diagonals)
}

fun <T> Array<Array<T>>.getNeighbours(i: Int, j: Int, diagonals: Boolean = false): List<Pair<Pair<Int, Int>, T>> {
    val neighbours = getNeighbourCoords(i, j, diagonals)
    return neighbours.filter { this.isValidCoord(it.first, it.second) }.map { it to this[it.first][it.second] }
}

fun <T> Array<Array<T>>.isValidCoord(i: Int, j: Int): Boolean {
    return i > -1 && j > -1 && i < this.size && j < this.first().size
}

fun <T> Array<Array<T>>.isValidCoord(pos: Pair<Int, Int>): Boolean {
    return isValidCoord(pos.first, pos.second)
}

fun <T> Array<Array<T>>.tryMove(from: Pair<Int, Int>, direction: Pair<Int, Int>): Pair<T?, Pair<Int, Int>> {
    val (x, y) = from.first + direction.first to from.second + direction.second
    return if(this.isValidCoord(x, y)) {
        this[x][y] to (x to y)
    } else {
        null to (x to y)
    }
}

fun <T> Array<Array<T>>.draw(from: Pair<Int, Int>, to: Pair<Int, Int>, value: T) {
    if (from.first == to.first) {
        val coords = listOf(from.second, to.second).sorted()
        (coords[0]..coords[1]).forEach {
            this[from.first][it] = value
        }
    } else if (from.second == to.second) {
        val coords = listOf(from.first, to.first).sorted()
        (coords[0]..coords[1]).forEach {
            this[it][from.second] = value
        }
    } else {
        throw Exception("Coordinates do not match: $from, $to")
    }
}

fun <T> Array<Array<T>>.print() {
    for (i in this.first().indices) {
        for (j in this.indices) {
            print(this[j][i])
        }
        println()
    }
    println()
}

fun <T> List<List<T>>.getNeighbours(i: Int, j: Int): List<Pair<Pair<Int, Int>, T>> {
    val neighbours =
        listOf(i to j + 1, i to j - 1, i + 1 to j, i - 1 to j).filter { this.isValidCoord(it.first, it.second) }
    return neighbours.map { it to this[it.first][it.second] }
}

fun <T> Collection<Collection<T>>.isValidCoord(i: Int, j: Int): Boolean {
    return i > -1 && j > -1 && i < this.size && j < this.first().size
}

infix fun Pair<IntProgression, IntProgression>.intersect(other: Pair<IntProgression, IntProgression>): Pair<IntProgression, IntProgression> {
    return (this.first intersect other.first) to (this.second intersect other.second)
}

operator fun Pair<IntProgression, IntProgression>.minus(other: Pair<IntProgression, IntProgression>): List<Pair<IntProgression, IntProgression>> {
    val xs = this.first - other.first
    val ys = this.second - other.second
    val safeAreas = xs.flatMap { x -> ys.map { x to it } }
    //val emptyArea = 0..-1 to 0..-1

    val mappedYs = ys.map { this.first to it }//.map { safeAreas.fold(it) { acc, a -> (acc - a).firstOrNull() ?: emptyArea } }
    val mappedXs = xs.map { it to this.second }//.map { safeAreas.fold(it) { acc, a -> (acc - a).firstOrNull() ?: emptyArea } }
        return safeAreas
            .plus(mappedXs)
            .plus(mappedYs)
            .filter { !it.first.isEmpty() && !it.second.isEmpty() }
}

fun Pair<Int, Int>.adjacent(direction: CardinalDirection): Pair<Int, Int> {
    return when (direction) {
        CardinalDirection.East -> x() + 1 to y()
        CardinalDirection.South -> x() to y() + 1
        CardinalDirection.West -> x() - 1 to y()
        CardinalDirection.North -> x() to y() - 1
    }
}

fun Pair<Int, Int>.cardinalDirectionTo(other: Pair<Int, Int>): CardinalDirection {
    if(this == other)
        throw Exception("No direction to self")
    return if(this.x() == other.x())
        if(this.y() < other.y()) CardinalDirection.South else CardinalDirection.North
    else if(this.y() == other.y())
        if(this.x() < other.x()) CardinalDirection.East else CardinalDirection.West
    else throw Exception("Direction from $this to $other is not cardinal")
}

enum class CardinalDirection(val value: Int) {
    East(0),
    South(1),
    West(2),
    North(3);

    fun turn(change: RelativeDirection): CardinalDirection {
        return when (change) {
            RelativeDirection.Up -> this
            RelativeDirection.Down -> this.opposite()
            RelativeDirection.Left -> CardinalDirection.values().first { it.value == (this.value - 1).wrapAt(4) }
            RelativeDirection.Right -> CardinalDirection.values().first { it.value == (this.value + 1) % 4 }
        }
    }

    fun opposite(): CardinalDirection {
        return when(this) {
            East -> West
            West -> East
            North -> South
            South -> North
        }
    }
}

enum class RelativeDirection(val char: Char) {
    Up('U'),
    Down('D'),
    Left('L'),
    Right('R')
}
fun relativeDirectionTo(orientation: CardinalDirection, target: CardinalDirection): RelativeDirection {
    return when(orientation) {
        CardinalDirection.North -> when(target) {
            CardinalDirection.North -> RelativeDirection.Up
            CardinalDirection.South -> RelativeDirection.Down
            CardinalDirection.West -> RelativeDirection.Left
            CardinalDirection.East -> RelativeDirection.Right
        }
        CardinalDirection.South -> when(target) {
            CardinalDirection.North -> RelativeDirection.Down
            CardinalDirection.South -> RelativeDirection.Up
            CardinalDirection.West -> RelativeDirection.Right
            CardinalDirection.East -> RelativeDirection.Left
        }
        CardinalDirection.West -> when(target) {
            CardinalDirection.North -> RelativeDirection.Right
            CardinalDirection.South -> RelativeDirection.Left
            CardinalDirection.West -> RelativeDirection.Up
            CardinalDirection.East -> RelativeDirection.Down
        }
        CardinalDirection.East -> when(target) {
            CardinalDirection.North -> RelativeDirection.Left
            CardinalDirection.South -> RelativeDirection.Right
            CardinalDirection.West -> RelativeDirection.Down
            CardinalDirection.East -> RelativeDirection.Up
        }
    }
}

fun <T> Pair<T, T>.x() = first
fun <T> Pair<T, T>.y() = second

fun Pair<Int, Int>.wrapAt(limits: Pair<Int, Int>): Pair<Int, Int> {
    val x = this.x().wrapAt(limits.x())
    val y = this.y().wrapAt(limits.y())
    return x to y
}