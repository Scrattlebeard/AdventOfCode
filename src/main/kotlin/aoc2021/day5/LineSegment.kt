package aoc2021.day5

class LineSegment(points: Pair<Pair<Int, Int>, Pair<Int, Int>>) {
    val start = if (points.first.first <= points.second.first) points.first else points.second
    val end = if (points.first.first <= points.second.first) points.second else points.first
    private val vertDir = if (start.second < end.second) 1 else -1

    fun getPoints(): Set<Pair<Int, Int>> {
        val points = if (start.second == end.second) {
            val r = start.first..end.first
            (r.map { Pair(it, start.second) }).toSet()
        } else if(start.first == end.first) {
            val r = minOf(start.second, end.second)..maxOf(start.second, end.second)
            (r.map { Pair(start.first, it) }).toSet()
        }
        else {
            val r1 = start.first..end.first
            val r2 = if(vertDir > 0) start.second..end.second else start.second downTo end.second
            r1.zip(r2).toSet()
        }

        return points
    }
}