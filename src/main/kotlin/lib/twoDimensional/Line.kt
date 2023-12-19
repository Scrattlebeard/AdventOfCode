package lib.twoDimensional

import kotlin.math.abs

data class Line (val from: Point, val to: Point) {

    fun size(): Long {
        return abs(to.x - from.x) + abs(to.y - from.y) + 1
    }

    fun getPoints(): Sequence<Point> = sequence {
        (minOf(to.x, from.x)..maxOf(to.x, from.x)).forEach { x ->
            (minOf(to.y, from.y)..maxOf(to.y, from.y)).forEach { y ->
                yield(Point(x, y))
            }
        }
    }
}

