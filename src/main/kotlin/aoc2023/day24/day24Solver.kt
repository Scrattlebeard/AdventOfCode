package aoc2023.day24

import lib.*
import lib.threeDimensional.LongPoint

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Hail>> {
    return setup {
        day(24)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines()
                .map { it.split(" @ ") }
                .map { it[0].split(",").map { it.trim().toLong() } to it[1].split(",").map { it.trim().toLong() }}
                .map { Hail(LongPoint(it.first[0], it.first[1], it.first[2]), LongPoint(it.second[0], it.second[1], it.second[2])) }
        }

        partOne {
            val hails = it
            val min = 200000000000000.0
            val max = 400000000000000.0
            val intersections = hails.mapIndexed { i, hail -> hail to hails.drop(i+1).map {it to it.intersect2d(hail) } }
            val relevant = intersections.flatMap { pair -> pair.second.filter { it.second != null && pair.first.isOnFuturePath(it.second!!.first) && it.first.isOnFuturePath(it.second!!.first) } }
            //relevant.map { it.second!! }.count { it.first in 7.0..27.0 && it.second in 7.0..27.0 }.toString()
            relevant.map { it.second!! }.count { it.first in min..max && it.second in min..max }.toString()
        }

        partTwo {
            null
        }
    }
}

data class Hail(val currentPosition: LongPoint, val delta: LongPoint) {
    val ySlope = delta.y.toDouble() / delta.x
    val yStart = currentPosition.y - (ySlope * currentPosition.x)

    fun intersect2d(other: Hail): Pair<Double, Double>? {
        if(this.ySlope == other.ySlope && yStart != other.yStart) {
            return null
        }
        val x = (other.yStart - yStart) / (ySlope - other.ySlope)
        return x to ySlope * x + yStart
    }

    fun isOnFuturePath(x: Double): Boolean {
        return if(delta.x > 0) x > currentPosition.x else x < currentPosition.x
    }
}
