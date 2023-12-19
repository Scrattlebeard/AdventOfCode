package aoc2023.day18

import lib.*
import lib.twoDimensional.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Instruction>> {
    return setup {
        day(18)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines()
                .map {
                    val parts = it.split(" ")
                    Instruction(
                        charToDirection[parts[0][0]]!!,
                        parts[1].toInt(),
                        parts[2].replace("(", "").replace(")", "")
                    )
                }
        }

        partOne {
            var currentPos = Point(0, 0)
            val lines = mutableListOf<Line>()
            it.forEach {
                val newPos = currentPos + (it.direction.movementDelta() * it.dist.toLong())
                lines.add(Line(currentPos, newPos))
                currentPos = newPos
            }

            val inside = mutableListOf<LongRange>()
            val horizontalLines = lines.filter { it.from.x != it.to.x }
            val verticalLines = lines.filter { it.from.y != it.to.y }
            horizontalLines.filter { it.from.cardinalDirectionTo(it.to) == CardinalDirection.East }.forEach {
                val start = it.from.y
                val otherLines = horizontalLines.filter { it.from.y > start }

                (it.from.x .. it.to.x).forEach { x ->
                    val limit =
                        otherLines.filter { (minOf(it.from.x, it.to.x)..maxOf(it.from.x, it.to.x)).contains(x) }
                            .minOfOrNull { it.from.y }
                    if (limit != null) {
                        val  candidate = start + 1 until limit
                        if(verticalLines.none { it.from.x == x && candidate.intersects(minOf(it.from.y, it.to.y)..maxOf(it.from.y, it.to.y)) }) {
                            inside.add(start + 1 until limit)
                        }
                    }
                }

            }

            (inside.sumOf { it.size() } + lines.sumOf { it.size() } - lines.count() ).toString()
        }

        partTwo {

            val realInstructions = it.map {
                Instruction(hexDigitToDirection[it.color.last()]!!, it.color.drop(1).dropLast(1).toInt(16), it.color)
            }

            var currentPos = Point(0, 0)
            val lines = mutableListOf<Line>()
            realInstructions.forEach {
                val newPos = currentPos + (it.direction.movementDelta() * it.dist.toLong())
                lines.add(Line(currentPos, newPos))
                currentPos = newPos
            }

            val inside = mutableListOf<LongRange>()
            val horizontalLines = lines.filter { it.from.x != it.to.x }
            val verticalLines = lines.filter { it.from.y != it.to.y }
            horizontalLines.filter { it.from.cardinalDirectionTo(it.to) == CardinalDirection.East }.forEach {
                val start = it.from.y
                val otherLines = horizontalLines.filter { it.from.y > start }

                (it.from.x .. it.to.x).forEach { x ->
                    val limit =
                        otherLines.filter { (minOf(it.from.x, it.to.x)..maxOf(it.from.x, it.to.x)).contains(x) }
                            .minOfOrNull { it.from.y }
                    if (limit != null) {
                        val  candidate = start + 1 until limit
                        if(verticalLines.none { it.from.x == x && candidate.intersects(minOf(it.from.y, it.to.y)..maxOf(it.from.y, it.to.y)) }) {
                            inside.add(start + 1 until limit)
                        }
                    }
                }

            }

            (inside.sumOf { it.size() } + lines.sumOf { it.size() } - lines.count() ).toString()
        }
    }
}

val charToDirection = mapOf('U' to CardinalDirection.North, 'D' to CardinalDirection.South, 'L' to CardinalDirection.West, 'R' to CardinalDirection.East)
val hexDigitToDirection = mapOf('0' to CardinalDirection.East, '1' to CardinalDirection.South, '2' to CardinalDirection.West, '3' to CardinalDirection.North)

data class Instruction(val direction: CardinalDirection, val dist: Int, val color: String)