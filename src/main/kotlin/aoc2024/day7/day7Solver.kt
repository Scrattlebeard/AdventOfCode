package aoc2024.day7

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Equation>> {
    return setup {
        day(7)
        year(2024)

        parser {
            it.readLines()
                .map { it.split(":") }
                .map { it[0].toLong() to it[1].split(" ").filter { it.isNotEmpty() }}
                .map { Equation(it.first, it.second.map { it.toLong() }) }
        }

        partOne {
            it.filter { it.canSolve() }
                .sumOf { it.result }
                .toString()
        }

        partTwo {
            it.filter { it.canSolveWithConcat() }
                .sumOf { it.result }
                .toString()
        }
    }
}

data class Equation(val result: Long, val numbers: List<Long>)

fun Equation.canSolve(): Boolean {
    return if(this.numbers.size == 1) {
        this.result == numbers.first()
    } else {
        val first = numbers.first()
        val second = numbers[1]
        val rest = numbers.drop(2)
        Equation(this.result, listOf(first * second).plus(rest)).canSolve()
                || Equation(this.result, listOf(first + second).plus(rest)).canSolve()
    }
}

fun Equation.canSolveWithConcat(): Boolean {
    return if(this.numbers.size == 1) {
        this.result == numbers.first()
    } else {
        val first = numbers.first()
        val second = numbers[1]
        val rest = numbers.drop(2)
        Equation(this.result, listOf(first * second).plus(rest)).canSolveWithConcat()
                || Equation(this.result, listOf(first + second).plus(rest)).canSolveWithConcat()
                || Equation(this.result, listOf((first.toString() + second.toString()).toLong()).plus(rest)).canSolveWithConcat()
    }
}