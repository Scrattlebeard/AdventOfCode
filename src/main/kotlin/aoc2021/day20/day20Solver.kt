package aoc2021.day20

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<String, Array<Array<Char>>>> {
    return setup {
        day(20)
        year(2021)

        //input("example.txt")

        parser {
            val inp = it.getStringsGroupedByEmptyLine()
            inp[0][0] to inp[1].get2DArrayOfColumns().addBorderOf('.')
        }

        partOne {
            val res = it.second.enhance(it.first).enhance(it.first)
            res.sumOf { it.count { it == '#' } }.toString()
        }

        partTwo {
            var res = it.second
            for (i in 0 until 50) {
                res = res.enhance(it.first)
            }
            res.sumOf { it.count { it == '#' } }.toString()
        }
    }
}

fun Array<Array<Char>>.enhance(rule: String): Array<Array<Char>> {
    val defaultChar = this[0][0]
    val borderChar = rule[List(9) { defaultChar }.findBinaryValue()]
    return Array(this.size) { i ->
        Array(this.first().size) { j ->
            val coords = getNeighbourCoords(i, j, true).plus(i to j)
                .sortedBy { it.first }
                .sortedBy { it.second }
            val chars = coords.map {
                if (this.isValidCoord(it.first, it.second)) this[it.first][it.second] else defaultChar
            }
            val index = chars.findBinaryValue()
            rule[index]
        }
    }.addBorderOf(borderChar)
}

fun Array<Array<Char>>.addBorderOf(c: Char): Array<Array<Char>> {
    return Array(this.size + 2) { i ->
        when (i) {
            0 -> Array(this.first().size + 2) { c }
            this.size + 1 -> Array(this.first().size + 2) { c }
            else -> arrayOf(c).plus(this[i - 1]).plus(arrayOf(c))
        }
    }
}

fun Iterable<Char>.findBinaryValue(): Int {
    return this.map { if (it == '#') 1 else 0 }
        .fold("") { acc, digit -> acc + digit }
        .toInt(2)
}