package aoc2024.day3

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<String> {
    return setup {
        day(3)
        year(2024)

        parser {
            it.readText()
        }

        partOne {
            executeMulOperations(it).toString()
        }

        partTwo {
            val disabledPattern = """don't\(\).*?(?:do\(\)|$)""".toRegex(RegexOption.DOT_MATCHES_ALL)
            val parsedString = it.replace(disabledPattern, "")
            executeMulOperations(parsedString).toString()
        }
    }
}

fun executeMulOperations(input: String): Int {
    val mulPattern = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    return mulPattern.findAll(input).map {
        val (a, b) = it.destructured
        a.toInt() to b.toInt()
    }
        .sumOf { (a, b) -> a * b }
}