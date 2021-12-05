package aoc2021.day3

import lib.*

suspend fun main() {
    challenge<List<List<Int>>> {

        day(3)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines().readStringsAsDigits()
        }

        solver {
            println("Solving challenge 1...")
            val gamma = mostCommonDigits(it)
            val epsilon = gamma.flip()
            val consumption = gamma.toBinaryInt() * epsilon.toBinaryInt()
            consumption.toString()
        }

        solver {
            println("Solving challenge 2...")
            val oxygenRating = findOxygenRating(it)
            val co2rating = findCO2Rating(it)
            (oxygenRating * co2rating).toString()
        }
    }
}

fun elementwiseSum(l: List<List<Int>>): List<Int> {
    return l.fold(List(l.first().size) { 0 }) { acc, ele -> addElements(acc, ele) }
}

fun addElements(l1: List<Int>, l2: List<Int>): List<Int> {
    return l1.mapIndexed { i, ele -> ele + l2[i] }
}

fun mostCommonDigits(l: List<List<Int>>): List<Int> {
    val sums = elementwiseSum(l)
    return sums.map {
        if (it < l.size / 2.0) 0 else 1
    }
}

fun List<Int>.toBinaryInt(): Int {
    return this.map(Int::toString).fold("") { acc, ele -> acc + ele }.toInt(2)
}

fun List<List<Int>>.filterBy(i: Int, value: Int): List<List<Int>> {
    return this.filter { it[i] == value }
}

fun findOxygenRating(inp: List<List<Int>>): Int {
    var l = inp
    var i = 0
    while (l.size > 1) {
        val mcd = mostCommonDigits(l)
        l = l.filterBy(i, mcd[i])
        i++
    }
    return (l.first().toBinaryInt())
}

fun findCO2Rating(inp: List<List<Int>>): Int {
    var l = inp
    var i = 0
    while (l.size > 1) {
        val mcd = mostCommonDigits(l)
        l = l.filterBy(i, mcd[i].flip())
        i++
    }
    return (l.first().toBinaryInt())
}

fun List<Int>.flip(): List<Int> {
    return this.map { it.flip() }
}

fun Int.flip(): Int {
    return this * (-1) + 1
}