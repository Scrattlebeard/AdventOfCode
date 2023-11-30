package aoc2021.day17

import lib.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    return setup {
        day(17)
        year(2021)

        parser {
            val (xMin, xMax, yMin, yMax) = """target area: x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)"""
                .toRegex()
                .find(it.readText())!!
                .destructured

            if (yMax.toInt() > 0) {
                throw Exception("Target above sub not supported")
            }

            (xMin.toInt() to xMax.toInt()) to (yMin.toInt() to yMax.toInt())
        }

        partOne {

            //The height during rise and drop is perfectly symmetrical, so we end up back at (_,0) with a downwards velocity of -(angle + 1).
            //Unlike real physics, the deltaX has no bearing on the height of the curve
            val bestAngle = -(it.second.first + 1)
            sumOfIntsUntil(bestAngle).toString()
        }

        partTwo {

            //We want to fire with a velocity that is fast enough to reach
            //Approximation based on 1 + 2 + ... n = (n^2 + n) / 2
            val minSpeed = sqrt(2.0 * it.first.first).roundToInt() - 1
            val maxSpeed = it.first.second

            val maxAngle = -(it.second.first + 1)
            val minAngle = it.second.first

            var hits = 0

            for (speed in minSpeed..maxSpeed) {
                hits += (minAngle..maxAngle).count { angle -> fireDrone(speed, angle, it) }

            }
            hits.toString()
        }
    }
}

fun sumOfIntsUntil(n: Int): Long {
    return (n * (n + 1).toDouble() / 2).roundToLong()
}

//Could be optimized heavily with More Math(tm) but I can't be bothered
fun fireDrone(startX: Int, startY: Int, target: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
    var pos = (0 to 0)
    var deltaX = startX
    var deltaY = startY
    val xMax = target.first.second
    val yMin = target.second.first
    while (pos.first <= xMax && pos.second >= yMin) {

        pos = pos.first + deltaX to pos.second + deltaY

        if (deltaX > 0) {
            deltaX--
        }
        deltaY--

        if (isWithinSquare(pos, target.first to target.second)) {
            return true
        }
    }
    return false
}