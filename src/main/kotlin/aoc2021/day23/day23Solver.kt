package aoc2021.day23

import lib.*
import kotlin.math.abs

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Burrow> {
    return setup {
        day(23)
        year(2021)

        parser {
            //Example:
            //Burrow(arrayOf('B', 'A'), arrayOf('C', 'D'), arrayOf('B', 'C'), arrayOf('D', 'A'))
            //Input:
            Burrow(arrayOf('B', 'C'), arrayOf('C', 'D'), arrayOf('A', 'D'), arrayOf('B', 'A'))

        }

        partOne {
            val moves = tryMoves(it, listOf())
            moves.sumOf { it.second }.toString()
        }

        partTwo {
            val realBurrow = Burrow(
                arrayOf(it.roomA[0], 'D', 'D', it.roomA[1]),
                arrayOf(it.roomB[0], 'C', 'B', it.roomB[1]),
                arrayOf(it.roomC[0], 'B', 'A', it.roomC[1]),
                arrayOf(it.roomD[0], 'A', 'C', it.roomD[1])
            )

            //TODO: Find best solution instead of a solution
            val moves = tryMoves(realBurrow, listOf())
            moves.sumOf { it.second }.toString()
        }
    }
}

fun tryMoves(burrow: Burrow, takenMoves: List<Pair<String, Int>>): List<Pair<String, Int>> {
    burrow.getActivePieces().forEach { (piece, moves) ->
        moves.sortedBy { it.second }.forEach { (move, cost) ->
            val moveRepr = "${piece.loc.room}${piece.loc.i} to ${move.room}${move.i}" to cost
            val updBurrow = burrow.takeMove(piece, move)
            if (updBurrow.isDone()) {
                return takenMoves.plus(moveRepr)
            }
            val newMoves = tryMoves(updBurrow, takenMoves.plus(moveRepr))
            if (newMoves.any()) {
                return newMoves
            }
        }
    }
    return listOf()
}

class Burrow(val roomA: Array<Char>, val roomB: Array<Char>, val roomC: Array<Char>, val roomD: Array<Char>) {

    var hallway = Array(11) { '.' }
    val roomEntrances = mapOf('A' to 2, 'B' to 4, 'C' to 6, 'D' to 8)

    fun takeMove(piece: Piece, to: Location): Burrow {
        val newMap = Burrow(roomA.copyOf(), roomB.copyOf(), roomC.copyOf(), roomD.copyOf())
        newMap.hallway = hallway.copyOf()
        newMap.getRoom(piece.loc.room)[piece.loc.i] = '.'
        newMap.getRoom(to.room)[to.i] = piece.destination
        return newMap
    }

    fun isDone(): Boolean {
        return hallway.all { it == '.' }
                && roomA.all { it == 'A' }
                && roomB.all { it == 'B' }
                && roomC.all { it == 'C' }
                && roomD.all { it == 'D' }
    }

    fun getRoom(room: Char): Array<Char> {
        return when (room) {
            'H' -> hallway
            'A' -> roomA
            'B' -> roomB
            'C' -> roomC
            'D' -> roomD
            else -> throw Exception("Unknown room: $room")
        }
    }

    fun checkRoom(c: Char): Boolean {
        val res = getRoom(c).all { it == '.' || it == c }
        if (res) {
            //println("Solved room $c")
        }
        return res
    }

    fun getActivePieces(): Sequence<Pair<Piece, List<Pair<Location, Int>>>> {
        var pieces = hallway.getPieces('H')
        pieces = listOf('A', 'B', 'C', 'D').fold(pieces) { acc, c ->
            if (checkRoom(c))
                acc
            else
                acc.plus(getRoom(c).getPieces(c).first())
        }
        return pieces.asSequence().map { it to it.getMoves() }.filter { it.second.isNotEmpty() }
    }

    fun Array<Char>.getPieces(loc: Char): List<Piece> {
        return this.mapIndexed { i, c -> if (c != '.') Piece(c, Location(loc, i)) else null }.filterNotNull()
    }

    fun Piece.getMoves(): List<Pair<Location, Int>> {
        if (checkRoom(destination) && isPathClear(loc, Location(destination, 0))) {
            //Just move to dest if possible
            val loc = Location(destination, getRoom(destination).lastIndexOf('.'))
            return listOf(loc to computeCost(this, loc))
        }

        if (this.loc.room == 'H') {
            //Hallway, only moves to destination allowed
            return listOf()
        }

        val exit = roomEntrances[this.loc.room]!!
        val leftOptions = hallway.sliceArray(0 until exit)
            .mapIndexed { i, _ -> Location('H', i) }
        val rightOptions = hallway.sliceArray(exit + 1 until hallway.size)
            .mapIndexed { i, _ -> Location('H', i + exit + 1) }
        return leftOptions
            .plus(rightOptions)
            .filter { isPathClear(this.loc, it) && it.i !in roomEntrances.values }
            .map { it to computeCost(this, it) }
    }

    fun isPathClear(from: Location, to: Location): Boolean {
        if (from == to) {
            return true
        }
        if (from.room == 'H' && to.room == 'H') {
            return false //Never move in corridor
        }
        if (from.room == 'H') {
            return isHallwayClear(from.i, roomEntrances[to.room]!!) && getRoom(to.room)[0] == '.'
        }
        if (to.room == 'H') {
            return isHallwayClear(roomEntrances[from.room]!!, to.i) //&& isRoomClear(from.room, 0, from.i)
        }
        return /*isRoomClear(from.room, 0, from.i)
                &&*/ isHallwayClear(roomEntrances[from.room]!!, roomEntrances[to.room]!!)
                && getRoom(to.room)[0] == '.'
    }

    fun isHallwayClear(from: Int, to: Int): Boolean {
        val hLoc = if (from < to) from + 1 else from - 1
        val res = hallway.sliceArray(minOf(hLoc, to)..maxOf(hLoc, to)).all { it == '.' }
        return res
    }

    fun isRoomClear(room: Char, from: Int, to: Int): Boolean {
        val res = getRoom(room).sliceArray(minOf(from, to)..maxOf(from, to)).all { it == '.' }
        return res
    }

    fun computeCost(piece: Piece, loc: Location): Int {
        val squares = countSquares(piece.loc, loc)
        return when (piece.destination) {
            'A' -> squares
            'B' -> squares * 10
            'C' -> squares * 100
            'D' -> squares * 1000
            else -> throw Exception("Unknown cost for piece: ${piece.destination}")
        }
    }

    fun countSquares(from: Location, to: Location): Int {
        if (from.room != 'H') {
            return from.i + 1 + countSquares(Location('H', roomEntrances[from.room]!!), to)
        }
        if (to.room != 'H') {
            return to.i + 1 + countSquares(from, Location('H', roomEntrances[to.room]!!))
        }
        return abs(to.i - from.i)
    }
}

class Piece(val destination: Char, val loc: Location)

class Location(val room: Char, val i: Int) {
    override fun equals(other: Any?): Boolean {
        if (other is Location) {
            return other.room == this.room && other.i == this.i
        }
        return false
    }

    override fun hashCode(): Int {
        var result = room.hashCode()
        result = 31 * result + i
        return result
    }
}