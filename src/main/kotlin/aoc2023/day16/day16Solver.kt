package aoc2023.day16

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(16)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines().get2DArrayOfColumns()
        }

        partOne {
            val res = it.traceBeam(Beam(0 to 0, CardinalDirection.East))
            res.distinctBy { it.pos }.size.toString()
        }

        partTwo {
            val dimensions = it.size - 1 to it.first().size - 1
            val verticalRes = it.indices.flatMap {
                listOf(
                    Beam(it to 0, CardinalDirection.South),
                    Beam(it to dimensions.second, CardinalDirection.North)
                )
            }
            val horizontalRes = it.first().indices.flatMap {
                listOf(
                    Beam(0 to it, CardinalDirection.East),
                    Beam(dimensions.first to it, CardinalDirection.West)
                )
            }
            (verticalRes + horizontalRes).maxOf { beam -> it.traceBeam(beam).distinctBy { it.pos }.size }.toString()
        }
    }
}

data class Beam(var pos: Pair<Int, Int>, var direction: CardinalDirection)

fun Beam.move(): Beam {
    return Beam(pos.adjacent(this.direction), this.direction)
}

fun Beam.withDirection(direction: CardinalDirection): Beam {
    return Beam(this.pos, direction)
}

fun Beam.key(): String {
    return "${this.pos.first}:${this.pos.second}-${this.direction}"
}

val solutions = mutableMapOf<String, Set<Beam>>()

fun Array<Array<Char>>.traceBeam(beam: Beam): Set<Beam> {
    val toVisit = mutableSetOf(beam)
    val res = mutableSetOf<Beam>()
    while (toVisit.isNotEmpty()) {

        val next = toVisit.first()
        toVisit.remove(next)

        /*if (solutions.containsKey(next.key())) {
            res.addAll(solutions[next.key()]!!)
        } else*/ if (isValidCoord(next.pos) && next !in res) {
            res.add(next)

            when (this[next.pos.first][next.pos.second]) {
                '.' -> toVisit.add(next.move())
                '\\' -> {
                    next.direction = when (next.direction) {
                        CardinalDirection.North -> CardinalDirection.West
                        CardinalDirection.East -> CardinalDirection.South
                        CardinalDirection.West -> CardinalDirection.North
                        CardinalDirection.South -> CardinalDirection.East
                    }
                    toVisit.add(next.move())
                }

                '/' -> {
                    next.direction = when (next.direction) {
                        CardinalDirection.North -> CardinalDirection.East
                        CardinalDirection.West -> CardinalDirection.South
                        CardinalDirection.East -> CardinalDirection.North
                        CardinalDirection.South -> CardinalDirection.West
                    }
                    toVisit.add(next.move())
                }

                '|' -> {
                    when (next.direction) {
                        CardinalDirection.West, CardinalDirection.East -> {
                            toVisit.add(next.withDirection(CardinalDirection.North))
                            toVisit.add(next.withDirection(CardinalDirection.South))
                        }

                        else -> toVisit.add(next.move())
                    }
                }

                '-' -> {
                    when (next.direction) {
                        CardinalDirection.South, CardinalDirection.North -> {
                            toVisit.add(next.withDirection(CardinalDirection.East))
                            toVisit.add(next.withDirection(CardinalDirection.West))
                        }

                        else -> toVisit.add(next.move())
                    }
                }
            }
        }
    }
    //solutions[beam.key()] = res
    return res
}