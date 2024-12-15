package aoc2024.day6

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(6)
        year(2024)

        parser {
            it.readLines().get2DArrayOfColumns()
        }

        partOne {
            val visitedCoords = setOf<Pair<Int, Int>>().toMutableSet()
            var guard = it.findGuard()
            while (it.isValidCoord(guard.pos)) {
                visitedCoords.add(guard.pos)
                guard = it.moveGuard(guard)
            }
            visitedCoords.size.toString()
        }

        partTwo {
            var guard = it.findGuard()
            var next = it.moveGuard(guard)
            var loopCount = 0
            val triedPositions = setOf(guard.pos).toMutableSet()
            while (it.isValidCoord(next.pos)) {
                if (next.pos !in triedPositions && it.pathLoops(guard, next.pos)) {
                        loopCount++
                }
                triedPositions.add(next.pos)
                guard = next
                next = it.moveGuard(guard)
            }
            loopCount.toString()
        }
    }
}

data class Guard(val pos: Pair<Int, Int>, val direction: CardinalDirection)

val guardDirections = mapOf(
    '^' to CardinalDirection.North,
    '>' to CardinalDirection.East,
    'v' to CardinalDirection.South,
    '<' to CardinalDirection.West
)

fun Array<Array<Char>>.findGuard(): Guard {
    val x = this.indexOfFirst { it.any { it in guardDirections.keys } }
    val y = this[x].indexOfFirst { it in guardDirections.keys }
    return Guard((x to y), guardDirections.getValue(this[x][y]))
}

fun Array<Array<Char>>.moveGuard(guard: Guard): Guard {
    if (this.isValidCoord(guard.pos)) {
        val next = guard.pos.adjacent(guard.direction)
        return if (this.isValidCoord(next) && this.get(next) == '#') {
            Guard(guard.pos, guard.direction.turn(RelativeDirection.Right))
        } else {
            Guard(next, guard.direction)
        }
    }
    return guard
}

fun Array<Array<Char>>.pathLoops(guard: Guard, newObstacle: Pair<Int, Int>): Boolean {
    try {
        this[newObstacle.first][newObstacle.second] = '#'
        val visitedPositions = setOf<Guard>().toMutableSet()
        var currentPos = guard
        while (this.isValidCoord(currentPos.pos)) {
            if (visitedPositions.contains(currentPos)) {
                return true
            }
            visitedPositions.add(currentPos)
            currentPos = this.moveGuard(currentPos)
        }
        return false
    }
    finally {
        this[newObstacle.first][newObstacle.second] = '.'
    }
}