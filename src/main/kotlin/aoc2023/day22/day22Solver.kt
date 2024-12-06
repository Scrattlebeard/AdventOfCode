package aoc2023.day22

import lib.*
import lib.threeDimensional.Point

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Block>> {
    return setup {
        day(22)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines()
                .map {
                    it.split("~")
                        .map { it.split(",") }
                        .map { Point(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
                }
                .map { (it[0] to it[1]).toBlock() }
        }

        partOne {
            val sortedBlocks = it.sortedBy { it.start.z }
            val settledBlocks = mutableListOf<Block>()

            sortedBlocks.forEach { fallingBlock ->
                val support = settledBlocks.filter { it.overlaps(fallingBlock) }.maxOfOrNull { it.end.z } ?: 0
                val deltaZ = support - fallingBlock.start.z + 1
                settledBlocks.add(fallingBlock.moveZ(deltaZ))
            }
            settledBlocks.count { it.canBeRemoved(settledBlocks.minus(it)) }.toString()
        }

        partTwo {
            val sortedBlocks = it.sortedBy { it.start.z }
            val settledBlocks = mutableListOf<Block>()

            sortedBlocks.forEach { fallingBlock ->
                val support = settledBlocks.filter { it.overlaps(fallingBlock) }.maxOfOrNull { it.end.z } ?: 0
                val deltaZ = support - fallingBlock.start.z + 1
                settledBlocks.add(fallingBlock.moveZ(deltaZ))
            }

            settledBlocks.map {
                var otherBlocks = settledBlocks.minus(it)
                var fallenCount = 0

                var toFall = otherBlocks.firstOrNull { it.start.z != 1 && !it.hasSupport(otherBlocks) }
                while(toFall != null) {
                    fallenCount++
                    otherBlocks = otherBlocks.minus(toFall)
                    toFall = otherBlocks.firstOrNull { it.start.z != 1 && !it.hasSupport(otherBlocks) }
                }
                fallenCount
            }
                .sum().toString()
        }
    }
}

data class Block(val start: Point, val end: Point) {
    fun overlaps(other: Block): Boolean {
        return start.x..end.x intersects other.start.x..other.end.x && start.y..end.y intersects other.start.y..other.end.y
    }

    fun moveZ(n: Int): Block {
        return Block(start.moveZ(n), end.moveZ(n))
    }

    fun canBeRemoved(otherBlocks: List<Block>): Boolean {
        val supportedByThis = otherBlocks.filter { it.start.z == this.end.z + 1 && it.overlaps(this) }
        return supportedByThis.all { it.hasSupport(otherBlocks) }
    }

    fun hasSupport(otherBlocks: List<Block>): Boolean {
        return otherBlocks.any { it.end.z == this.start.z - 1 && it.overlaps(this) }
    }
}

fun Pair<Point, Point>.toBlock(): Block {
    return Block(
        listOf(this.first, this.second).minBy { it.x + it.y + it.z },
        listOf(this.first, this.second).maxBy { it.x + it.y + it.z })
}
