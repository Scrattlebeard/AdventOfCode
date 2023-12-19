package lib.twoDimensional

import lib.CardinalDirection

data class Point(val x: Long, val y: Long) {

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    operator fun times(scalar: Long): Point {
        return Point(x * scalar, y * scalar)
    }

    fun cardinalDirectionTo(other: Point): CardinalDirection {
        if (this == other)
            throw Exception("No direction to self")
        return if (x == other.x)
            if (y < other.y) CardinalDirection.South else CardinalDirection.North
        else if (y == other.y)
            if (x < other.x) CardinalDirection.East else CardinalDirection.West
        else throw Exception("Direction from $this to $other is not cardinal")
    }

    fun getAdjacentPoints(diagonals: Boolean = false): Set<Point> {
        val neighbours = listOf(Point(x, y + 1), Point(x, y - 1), Point(x + 1, y), Point(x - 1, y))
        val diagonalPoints = if (diagonals) listOf(
            Point(x + 1, y + 1),
            Point(x + 1, y - 1),
            Point(x - 1, y + 1),
            Point(x - 1, y - 1)
        ) else listOf()
        return (neighbours + diagonalPoints).toSet()
    }
}