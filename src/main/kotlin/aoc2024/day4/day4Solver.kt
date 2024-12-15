package aoc2024.day4

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(4)
        year(2024)

        //input("example.txt")

        parser {
            it.readLines().get2DArrayOfColumns()
        }

        partOne { puzzle ->
            val candidates = puzzle.findPositionsOf('X')
            candidates.sumOf { puzzle.countXmas(it) }.toString()
        }

        partTwo { puzzle ->
            val candidates = puzzle.findPositionsOf('A')
            candidates.count{ puzzle.isMasCross(it) }.toString()
        }
    }
}

fun Array<Array<Char>>.countXmas(pos: Pair<Int, Int>): Int {
    return this.getNeighbours(pos.first, pos.second, true).count { it.isXmas(this, 'X', it.first - pos) }
}

fun Pair<Pair<Int, Int>, Char>.isXmas(puzzle: Array<Array<Char>>, prev: Char, delta: Pair<Int, Int>): Boolean {
    val nextPos = this.first + delta
    if (!puzzle.isValidCoord(nextPos)) {
        return false
    }
    val nextChar = puzzle.get(nextPos)
    val next = nextPos to nextChar

    return when (this.second) {
        'M' -> prev == 'X' && nextChar == 'A' && next.isXmas(puzzle, 'M', delta)
        'A' -> prev == 'M' && nextChar == 'S'
        else -> false
    }
}

fun Array<Array<Char>>.isMasCross(pos: Pair<Int, Int>): Boolean {
    return this.isMas(pos.adjacent(CardinalDirection.North).adjacent(CardinalDirection.West), pos.adjacent(CardinalDirection.South).adjacent(CardinalDirection.East))
            && this.isMas(pos.adjacent(CardinalDirection.South).adjacent(CardinalDirection.West), pos.adjacent(CardinalDirection.North).adjacent(CardinalDirection.East))
}

fun Array<Array<Char>>.isMas(posA: Pair<Int, Int>, posB: Pair<Int, Int>): Boolean {
    return this.isValidCoord(posA)
            && this.isValidCoord(posB)
            && ((this.get(posA) == 'M' && this.get(posB) == 'S')
                || (this.get(posA) == 'S' && this.get(posB) == 'M')
            )
}