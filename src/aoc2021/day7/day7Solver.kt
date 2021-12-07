package aoc2021.day7

import lib.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

suspend fun main() {
    challenge<List<Int>> {

        day(7)

        input("example.txt")
        input("input.txt")

        parser {
            it.readText()
                .trim()
                .split(',')
                .map { s -> s.toInt() }
        }

        solver {
            println("Solving challenge 1...")
            val mean = it.sorted()[it.size / 2]
            it.sumOf { depth -> abs(depth - mean) }.toString()
        }

        solver {
            println("Solving challenge 2...")
            val depths = List(it.toSet().maxOf { it }){i -> i}
            val costs = depths.map { d -> costOf(d, it) }
            costs.minOf { it }.toString()
        }
    }
}

fun Int.sumOfIntsUntil(): Long {
    return (this *(this+1).toDouble()/2).roundToLong()
}

fun costOf(depth: Int, depths: List<Int>) : Long {
    return depths.sumOf {abs(it - depth).sumOfIntsUntil() }
}