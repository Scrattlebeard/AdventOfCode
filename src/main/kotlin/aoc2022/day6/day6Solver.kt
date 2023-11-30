package aoc2022.day6

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}
fun setupChallenge(): Challenge<Sequence<Char>> {
    return setup {
        day(6)
        year(2022)

        parser {
            it.readText().asSequence()
        }

        partOne {
            println("Solving challenge 1...")
            it.findSubstringWithoutRepeatingChars(4).toString()
        }

        partTwo {
            println("Solving challenge 2...")
            it.findSubstringWithoutRepeatingChars(14).toString()
        }
    }
}

fun Sequence<Char>.findSubstringWithoutRepeatingChars(n: Int): Int {
    return this
        .windowed(n)
        .mapIndexed { i, window -> window.all { c -> window.count { it == c } == 1 } to i + n }
        .first { it.first }
        .second
}