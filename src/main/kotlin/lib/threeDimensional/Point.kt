package lib.threeDimensional

import kotlin.math.*

data class Point(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point): Double {
        return sqrt((x.toDouble() - other.x).pow(2) + (y.toDouble() - other.y).pow(2) + (z.toDouble() - other.z).pow(2))
    }

    fun getNeighbours(): Set<Point> {
        return setOf(
            Point(x - 1, y, z),
            Point(x + 1, y, z),
            Point(x, y - 1, z),
            Point(x, y + 1, z),
            Point(x, y, z - 1),
            Point(x, y, z + 1))
    }

    fun moveZ(n: Int): Point {
        return Point(x, y, z + n)
    }
}

data class LongPoint(val x: Long, val y: Long, val z: Long) {
    fun distanceTo(other: LongPoint): Double {
        return sqrt((x.toDouble() - other.x).pow(2) + (y.toDouble() - other.y).pow(2) + (z.toDouble() - other.z).pow(2))
    }

    fun getNeighbours(): Set<LongPoint> {
        return setOf(
            LongPoint(x - 1, y, z),
            LongPoint(x + 1, y, z),
            LongPoint(x, y - 1, z),
            LongPoint(x, y + 1, z),
            LongPoint(x, y, z - 1),
            LongPoint(x, y, z + 1))
    }

    fun moveZ(n: Long): LongPoint {
        return LongPoint(x, y, z + n)
    }
}