package aoc2022.day22

import lib.*
import kotlin.math.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<Array<Array<Char>>, String>> {
    return setup {
        day(22)
        year(2022)

        input("example.txt")
        input("input.txt")

        parser {
            val parts = it.getStringsGroupedByEmptyLine()
            val map = parts[0].get2DArray(parts[0].size, parts[0].maxOf { it.length }, ' ')
            map to parts[1].first()
        }

        partOne {
            val startX = it.first.map { it.first() }.indexOf('.')
            val player = Player(startX to 0, CardinalDirection.East)
            player.followInstructions(it.second, it.first)
            (1000 * (player.pos.y() + 1) + 4 * (player.pos.x() + 1) + player.facing.value).toString()
        }

        partTwo {
            val dimensions = (it.first.sumOf { it.count { it != ' ' } } / 6.0).pow(1.0 / 2).roundToInt()
            val map = it.first
            val dice = Dice<Char>()

            val faces = Array(map.size / dimensions) { outer ->
                Array(map.first().size / dimensions) { inner ->
                    Array(dimensions) { x ->
                        Array(dimensions) { y ->
                            map[outer * dimensions + x][inner * dimensions + y]
                        }
                    }
                }
            }

            val start = faces.indexOfFirst { it[0].hasData() } to 0

            val done = mutableListOf<Pair<Int, Int>>()
            val todo =
                mutableListOf<Triple<Pair<Int, Int>, Pair<Int, CardinalDirection>?, String>>(Triple(start, null, ""))

            while (todo.any()) {
                var (current, connection, rotations) = todo.removeLast()

                var eyes = 1
                if (connection != null)
                    eyes = dice.connections[connection.first]!![connection.second]!!.first

                val relativeColumnIndex = current.first - start.first

                var face = faces[current.first][current.second]

                rotations.forEach {
                    when (it) {
                        'L' -> face = face.rotateClockwise()
                        'R' -> face = face.rotateCounterclockwise()
                        'I' -> face = face.invert()
                    }
                }

                if (current.second == 0) {
                    if (relativeColumnIndex > 0) {
                        face = face.rotateClockwise()
                        rotations += "L"
                    } else if (relativeColumnIndex < 0) {
                        face = face.rotateCounterclockwise()
                        rotations += "R"
                    }
                } else if (current.second == 2) {
                    if (relativeColumnIndex > 0) {
                        face = face.rotateCounterclockwise()
                        rotations += "L"
                    } else if (relativeColumnIndex < 0) {
                        face = face.rotateClockwise()
                        rotations += "R"
                    }
                } else if (current.second > 2) {
                    face = face.rotateClockwise().rotateClockwise() //Should be a mirroring?
                    rotations += "LL"
                }

                if (relativeColumnIndex.absoluteValue == 2) {
                    //face = face.mirror()
                    rotations += "M"
                }

                val offset = current.first * dimensions to current.second * dimensions
                dice.faces[eyes] = Face(face, rotations, offset, mutableMapOf())
                //dice.faces[eyes] = Face(face.map { it.map { if(it == '#') '.' else it }.toTypedArray() }.toTypedArray(), rotations, offset, mutableMapOf())

                done.add(current)
                val nextCoords = current.getNeighbours().filter {
                    faces.isValidCoord(it.first, it.second)
                            && faces[it.first][it.second].hasData()
                            && !done.contains(it)
                }
                nextCoords.forEach {
                    var relDirection = current.cardinalDirectionTo(it)
                    rotations.forEach {
                        when (it) {
                            'R' -> relDirection = relDirection.turn(RelativeDirection.Right)
                            'L' -> relDirection = relDirection.turn(RelativeDirection.Left)
                        }
                    }
                    todo.add(Triple(it, eyes to relDirection, rotations))
                }
            }

            //dice.faces.forEach { println(it.key); it.value.data.print() }

            /*(1..6).forEach { face ->
                println("Testing face transitions for $face...")
                CardinalDirection.values().forEach {dir ->
                    println("Testing $dir...")
                    var passed = true
                    (0..49).forEach {x ->
                        (0..49).forEach { y ->
                            val p = Player(x to y, dir)
                            p.face = face
                            p.followInstructions("200", dice)
                            passed = passed && p.pos == x to y && p.face == face && p.facing == dir
                            if (!passed) {
                                println("FAILED for start value ${x to y}. Result: ${p.pos} on face ${p.face}!")
                            }
                        }
                    }
                    println("Tested 200 steps $dir: ${if(passed) "Passed" else "FAILED!"}")
                    println()
                }
            }*/

            val player = Player(0 to 0, CardinalDirection.East)
            player.followInstructions(it.second, dice)
            println("Ended on face ${player.face} facing ${player.facing} with coords ${player.pos}")
            val (xOffset, yOffset) = dice.faces[player.face]!!.offset
            (1000 * (player.pos.y() + 1 + yOffset) + 4 * (player.pos.x() + 1 + xOffset) + player.facing.value).toString()
            //97445 too low
            //147245
        }
    }
}

fun Array<Array<Char>>.hasData(): Boolean {
    return this.any { it.any { it != ' ' } }
}

class Dice<T> {
    val faces = mutableMapOf<Int, Face<T>>()
    val connections = mapOf<Int, Map<CardinalDirection, Pair<Int, CardinalDirection>>>(
        1 to mapOf(
            CardinalDirection.North to (5 to CardinalDirection.South),
            CardinalDirection.East to (3 to CardinalDirection.South),
            CardinalDirection.South to (2 to CardinalDirection.South),
            CardinalDirection.West to (4 to CardinalDirection.South)
        ),
        2 to mapOf(
            CardinalDirection.North to (1 to CardinalDirection.North),
            CardinalDirection.East to (3 to CardinalDirection.East),
            CardinalDirection.South to (6 to CardinalDirection.South),
            CardinalDirection.West to (4 to CardinalDirection.West)
        ),
        3 to mapOf(
            CardinalDirection.North to (1 to CardinalDirection.West),
            CardinalDirection.East to (5 to CardinalDirection.East),
            CardinalDirection.South to (6 to CardinalDirection.West),
            CardinalDirection.West to (2 to CardinalDirection.West)
        ),
        4 to mapOf(
            CardinalDirection.North to (1 to CardinalDirection.East),
            CardinalDirection.East to (2 to CardinalDirection.East),
            CardinalDirection.South to (6 to CardinalDirection.East),
            CardinalDirection.West to (5 to CardinalDirection.West)
        ),
        5 to mapOf(
            CardinalDirection.North to (1 to CardinalDirection.South),
            CardinalDirection.East to (4 to CardinalDirection.East),
            CardinalDirection.South to (6 to CardinalDirection.North),
            CardinalDirection.West to (3 to CardinalDirection.West)
        ),
        6 to mapOf(
            CardinalDirection.North to (2 to CardinalDirection.North),
            CardinalDirection.East to (3 to CardinalDirection.North),
            CardinalDirection.South to (5 to CardinalDirection.North),
            CardinalDirection.West to (4 to CardinalDirection.North)
        )
    )
}

inline fun <reified T> Array<Array<T>>.rotateCounterclockwise(): Array<Array<T>> {
    return Array(this.first().size) { i -> Array(this.size) { j -> this[this.size - j - 1][i] } }
}

inline fun <reified T> Array<Array<T>>.rotateClockwise(): Array<Array<T>> {
    return Array(this.first().size) { i -> Array(this.size) { j -> this[j][this.first().size - i - 1] } }
}

inline fun <reified T> Array<Array<T>>.invert(): Array<Array<T>> {
    return Array(this.size) { i -> Array(this.first().size) { j -> this[i][this.first().size - j - 1] } }
}

inline fun <reified T> Array<Array<T>>.mirror(): Array<Array<T>> {
    return Array(this.size) { i -> Array(this.first().size) { j -> this[this.size - i - 1][j] } }
}

class Face<T>(
    val data: Array<Array<T>>,
    val rotations: String,
    val offset: Pair<Int, Int>,
    val connections: MutableMap<CardinalDirection, Face<T>>
)


class Player(var pos: Pair<Int, Int>, var facing: CardinalDirection) {

    var face = 1

    fun move(n: Int, map: Array<Array<Char>>) {
        repeat(n) {
            var next = pos.adjacent(facing).wrapAt(map.size to map.first().size)
            while (map[next.x()][next.y()] == ' ') {
                next = next.adjacent(facing).wrapAt(map.size to map.first().size)
            }

            if (map[next.x()][next.y()] == '#')
                return

            pos = next
        }
    }

    fun move(n: Int, dice: Dice<Char>) {
        val maxCoord = dice.faces[1]!!.data.size - 1
        repeat(n) {
            var next = pos.adjacent(facing)

            if (!dice.faces[face]!!.data.isValidCoord(next.x(), next.y())) {
                val nextFace = dice.connections[face]!![facing]!!
                val rotation = dice.faces[face]!!.rotations
                var dir = nextFace.second
                rotation.forEach {
                    when (it) {
                        'R' -> dir = dir.turn(RelativeDirection.Right)
                        'L' -> dir = dir.turn(RelativeDirection.Left)
                    }
                }

                next = when (facing) {
                    CardinalDirection.North -> when (nextFace.second) {
                        CardinalDirection.North -> {
                            pos.x() to maxCoord
                        }

                        CardinalDirection.South -> {
                            maxCoord - pos.x() to 0
                        }

                        CardinalDirection.East -> {
                            0 to pos.x()
                        }

                        CardinalDirection.West -> {
                            maxCoord to maxCoord - pos.x()
                        }
                    }

                    CardinalDirection.South -> when (nextFace.second) {
                        CardinalDirection.North -> {
                            maxCoord - pos.x() to maxCoord
                        }

                        CardinalDirection.South -> {
                            pos.x() to 0
                        }

                        CardinalDirection.East -> {
                            0 to maxCoord - pos.x()
                        }

                        CardinalDirection.West -> {
                            maxCoord to pos.x()
                        }
                    }

                    CardinalDirection.West -> when (nextFace.second) {
                        CardinalDirection.North -> {
                            maxCoord - pos.y() to maxCoord
                        }

                        CardinalDirection.South -> {
                            pos.y() to 0
                        }

                        CardinalDirection.East -> throw Exception("Unexpected facing change from West to East")
                        CardinalDirection.West -> {
                            maxCoord to pos.y()
                        }
                    }

                    CardinalDirection.East -> when (nextFace.second) {
                        CardinalDirection.North -> {
                            pos.y() to maxCoord
                        }

                        CardinalDirection.South -> {
                            maxCoord - pos.y() to 0
                        }

                        CardinalDirection.East -> {
                            0 to pos.y()
                        }

                        CardinalDirection.West -> throw Exception("Unexpected facing change from East to West")
                    }
                }


                /*var rotated = rotation.length % 2 == 1
                val mirrored = dice.faces[nextFace.first]!!.rotations.count{it == 'M'} % 2 == 1
                val realX = if(rotated) pos.y() else pos.x()
                val realY = if(rotated) pos.x() else pos.y()

                //CHeck for collision
                next = when(nextFace.second) {
                    CardinalDirection.North -> if(mirrored) maxCoord - realX to maxCoord else realX to maxCoord
                    CardinalDirection.South -> realX to 0
                    CardinalDirection.East -> 0 to realY
                    CardinalDirection.West -> maxCoord to realY
                }*/

                if (dice.faces[nextFace.first]!!.data[next.x()][next.y()] == '#') {
                    println("Movement to face ${nextFace.first} was blocked")
                    return
                }

                //Update positon
                face = nextFace.first
                facing = nextFace.second
                //println("Proceed to face $face at $next, facing $facing")

            } else if (dice.faces[face]!!.data[next.x()][next.y()] == '#') {
                println("Blocked at $pos on face $face")
                return
            }

            pos = next
            //println("Moving to pos $pos on face $face...")
        }
        //println("Moved to pos $pos on face $face")
    }

    fun turn(direction: Char) {
        facing = facing.turn(RelativeDirection.values().first { it.char == direction })
        //println("Turned $direction to face $facing")
    }

    fun followInstructions(instructions: String, map: Array<Array<Char>>) {
        var i = 0
        while (i < instructions.length) {
            if (instructions[i].isLetter()) {
                turn(instructions[i])
                i++
            } else {
                val dist = instructions.substring(i).takeWhile { it.isDigit() }
                move(dist.toInt(), map)
                i += dist.length
            }
        }
    }

    fun followInstructions(instructions: String, dice: Dice<Char>) {
        var i = 0
        while (i < instructions.length) {
            if (instructions[i].isLetter()) {
                turn(instructions[i])
                i++
            } else {
                val dist = instructions.substring(i).takeWhile { it.isDigit() }
                move(dist.toInt(), dice)
                i += dist.length
            }
        }
    }
}

