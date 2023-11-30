package aoc2022.day25

import lib.*
import kotlin.math.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<String>> {
    return setup {
        day(25)
        year(2022)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
        }

        partOne {
            it.sumOf { it.getSnafuValue() }.asSnafuString()
        }
    }
}

fun String.getSnafuValue(): Long {
    val values = this.reversed().mapIndexed { i, c -> 5.0.pow(i) * c.getSnafuValue() }
    return values.sumOf { it.toLong() }
}

fun Char.getSnafuValue(): Int {
    return when (this) {
        '-' -> -1
        '=' -> -2
        else -> this.digitToInt()
    }
}

fun Long.asSnafuString(): String {
    var limit = floor(this.log(5.0))
    if (this > 2 * 5.0.pow(limit)) {
        limit++
    }
    var rem = this
    var res = ""
    var current = limit
    while (rem != 0L) {
        val base = 5.0.pow(current).toLong()
        val toAdd = (rem / base.toDouble()).roundToLong()

        res += toAdd.toInt().asSnafuDigit()
        rem -= base * toAdd
        current--
    }
    while (current > -1) {
        res += 0
        current--
    }
    return res
}

fun Int.asSnafuDigit(): Char {
    return when (this) {
        -2 -> '='
        -1 -> '-'
        else -> this.toString().first()
    }
}