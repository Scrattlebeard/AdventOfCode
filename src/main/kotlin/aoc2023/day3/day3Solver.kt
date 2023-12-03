package aoc2023.day3

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(3)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines().get2DArrayOfColumns()
        }

        partOne {
            val partNumbers = mutableListOf<Int>()
            val schematic = it
            schematic.first().indices.forEach {j ->
                var currentNumber: String? = null
                var isPart = false

                schematic.indices.forEach {i ->
                    val char = schematic[i][j]
                    when {
                        char.isDigit() -> {
                            currentNumber = if(currentNumber == null) char.toString() else currentNumber + char
                            isPart = isPart ||
                                    listOf(Pair(-1, -1), Pair(0, -1), Pair(1, -1), Pair(1, 0), Pair(1, 1), Pair(0, 1), Pair(-1, 1))
                                    .any { schematic.tryMove(Pair(i, j), it).first.isSymbol() }
                        }
                        char.isSymbol() -> {
                            partNumbers.tryAdd(currentNumber)
                            isPart = true
                            currentNumber = ""
                        }
                        else -> {
                            if(isPart) {
                                partNumbers.tryAdd(currentNumber)
                            }
                            currentNumber = null
                            isPart = false
                        }
                    }
                }
                if(isPart) {
                    partNumbers.tryAdd(currentNumber)
                }
            }
            partNumbers.sum().toString()
        }

        partTwo {
            val gearRatios = mutableListOf<Long>()
            val schematic = it
            schematic.first().indices.forEach {j ->
                schematic.indices.forEach {i ->
                    if(schematic[i][j] == '*')
                    {
                        val numbers = schematic.getAdjacentNumbers(i, j)
                        if(numbers.size == 2) {
                            gearRatios.add(numbers[0] * numbers[1])
                        }
                    }
                }
            }
            gearRatios.sum().toString()
        }
    }
}

fun Char?.isSymbol(): Boolean {
    return this != null && this != '.' && !this.isDigit()
}

fun MutableList<Int>.tryAdd(ele: String?) {
    if(!ele.isNullOrEmpty()) {
        this.add(ele.toInt())
    }
}

fun Array<Array<Char>>.getAdjacentNumbers(i: Int, j: Int): List<Long> {
    val relevantNeighbours = this.getNeighbours(i, j, true).filter { it.second.isDigit() }
    val parsed = mutableListOf<Pair<Int, Int>>()
    val numbers = mutableListOf<Long>()

    relevantNeighbours.forEach {
        if(it.first !in parsed) {
            val (x, y) = it.first
            var start = x
            var end = x
            while(this.isValidCoord(start - 1, y) && this[start - 1][y].isDigit()) {
                start--
                parsed.add(Pair(start, y))
            }
            while(this.isValidCoord(end + 1, y) && this[end + 1][y].isDigit()) {
                end++
                parsed.add(Pair(end, y))
            }
            val number = (start .. end).map { this[it][y] }.fold(""){s, digit -> s + digit}.toLong()
            numbers.add(number)
        }
    }

    return numbers
}
