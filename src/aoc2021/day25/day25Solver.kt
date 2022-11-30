package aoc2021.day25

import lib.*

suspend fun main() {
    challenge<Array<Array<Char>>> {

        day(25)
        year(2021)

        parser {
            it.readLines().get2DArray()
        }

        solver {
            println("Solving challenge 1...")
            var floor = it
            var step = 0
            var changed = true
            while(changed) {
                val (updRight, floorRight) = floor.moveRight()
                val (updDown, floorDown) = floorRight.moveDown()
                changed = updRight || updDown
                floor = floorDown
                step++
            }
            step.toString()
        }

        solver {
            println("Solving challenge 2...")
            "Todo challenge 2"
        }
    }
}

fun Array<Array<Char>>.moveRight(): Pair<Boolean, Array<Array<Char>>> {
    val res = Array(this.size) { Array(this.first().size) { '.' } }
    var changed = false
    this.forEachIndexed { i, col ->
        col.forEachIndexed { j, c ->
            when (c) {
                '>' -> {
                    val destI = (i + 1) % res.size
                    if (this[destI][j] == '.') {
                        res[destI][j] = '>'
                        res[i][j] = '.'
                        changed = true
                    } else {
                        res[i][j] = '>'
                    }
                }
                'v' -> {
                    res[i][j] = 'v'
                }
                else -> {}
            }
        }
    }
    return changed to res
}

fun Array<Array<Char>>.moveDown(): Pair<Boolean, Array<Array<Char>>> {
    val res = Array(this.size) { Array(this.first().size) { '.' } }
    var changed = false
    this.forEachIndexed { i, col ->
        col.forEachIndexed { j, c ->
            when (c) {
                '>' -> {
                    res[i][j] = '>'
                }
                'v' -> {
                    val destJ = (j + 1) % res.first().size
                    if (this[i][destJ] == '.') {
                        res[i][destJ] = 'v'
                        res[i][j] = '.'
                        changed = true
                    } else {
                        res[i][j] = 'v'
                    }
                }
                else -> {}
            }
        }
    }
    return changed to res
}