package aoc2021.day10

import lib.*

suspend fun main() {
    challenge<List<String>> {

        day(10)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
        }

        solver {
            println("Solving challenge 1...")
            val scores = it.map { it.getSyntaxScore() }
                scores.sum()
                .toString()
        }

        solver {
            println("Solving challenge 2...")
            val scores = it.filter { it.getSyntaxScore() == 0 }
                .map {it.getAutocompleteScore()}
            scores.sorted()[scores.size / 2].toString()
        }
    }
}

fun String.getSyntaxScore() : Int {
    val points = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val chars = ArrayDeque<Char>()

    this.forEach {
        when(it) {
            '(' -> chars.addFirst(it)
            ')' -> if (chars.isEmpty() || chars.removeFirst() != '(') { return points[it]!! }
            '[' -> chars.addFirst(it)
            ']' -> if (chars.isEmpty() || chars.removeFirst() != '[') { return points[it]!! }
            '{' -> chars.addFirst(it)
            '}' -> if (chars.isEmpty() || chars.removeFirst() != '{') { return points[it]!! }
            '<' -> chars.addFirst(it)
            '>' -> if (chars.isEmpty() || chars.removeFirst() != '<') { return points[it]!! }
        }
    }
    return 0
}

fun String.getAutocompleteScore() : Long {
    val points = mapOf(')' to 1L, ']' to 2L, '}' to 3L, '>' to 4L)
    val chars = ArrayDeque<Char>()

    this.forEach {
        when(it) {
            '(' -> chars.addFirst(')')
            '[' -> chars.addFirst(']')
            '{' -> chars.addFirst('}')
            '<' -> chars.addFirst('>')
            else -> if (chars.isEmpty() || chars.removeFirst() != it) { return -1L }
        }
    }

    var score = 0L
    chars.forEach { score = score * 5 + points[it]!! }
    return score
}