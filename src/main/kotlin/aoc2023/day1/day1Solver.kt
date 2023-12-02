package aoc2023.day1

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<String>> {
    return setup {
        day(1)
        year(2023)

        parser {
            it.readLines()
        }

        partOne {
            val numbers = it.map {
                (it.first { it.isDigit() }.digitToInt() * 10
                + it.last { it.isDigit() }.digitToInt())
            }
            numbers.sum().toString()
        }

        partTwo {
            val calibrations = it.map {
                findFirst(it)!! * 10 + findLast(it)!!
            }
            calibrations.sum().toString()
        }
    }
}

val digits = mapOf(
    Pair("one", 1),
    Pair("two", 2),
    Pair("three", 3),
    Pair("four", 4),
    Pair("five", 5),
    Pair("six", 6),
    Pair("seven", 7),
    Pair("eight", 8),
    Pair("nine", 9)
)

fun findFirst(line: String): Int? {
    line.indices.forEach {
        val value = tryGetNumber(line, it)
        if (value != null) {
            return value
        }
    }
    return null
}

fun findLast(line: String): Int? {
    (line.length - 1 downTo 0).forEach {
        val value = tryGetNumber(line, it)
        if(value != null) {
            return value
        }
    }
    return null
}

fun tryGetNumber(line: String, index: Int): Int? {
    return if (line[index].isDigit()) {
        line[index].digitToInt()
    } else {
        val key = digits.keys.firstOrNull { line.substring(index).startsWith(it) }
        if(key != null) digits[key] else null
    }
}