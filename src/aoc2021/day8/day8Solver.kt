package aoc2021.day8

import lib.*

suspend fun main() {
    challenge<List<Pair<List<String>, List<String>>>> {

        day(8)
        year(2021)

        parser {
            it.readLines()
                .map { it.split('|') }
                .map { it.map { it.trim().split(' ') } }
                .map { Pair(it[0], it[1]) }
        }

        solver {
            println("Solving challenge 1...")
            it.map { it.second.count { it.getDigit() > -1 } }
                .sumOf { it }
                .toString()
        }

        solver {
            println("Solving challenge 2...")
            it
                .map { getOutput(it.first.toSet(), it.second) }
                .sumOf { it }
                .toString()
        }
    }
}

fun String.getDigit(): Int {
    return when (this.length) {
        2 -> 1
        4 -> 4
        3 -> 7
        7 -> 8
        else -> -1
    }
}

fun getOutput(input: Set<String>, output: List<String>): Int {
    val inputSets = input.map{it.toSet().sorted()}
    val knownDigits = input.associate { it.getDigit() to it.toSet().sorted() }.toMutableMap()
    //val a = knownDigits[7]!! subtract knownDigits[1]!!
    val cCandidates = inputSets.filter { it.size == 6 }
        .map { knownDigits[1]!! subtract (it.toSet() intersect knownDigits[1]!!) }
        val c = cCandidates.first { it.isNotEmpty() }
        .first()
    knownDigits[6] = inputSets.first { it.size == 6 && !it.contains(c) }.toSet().sorted()
    knownDigits[5] = inputSets.first { it.size == 5 && !it.contains(c) }.toSet().sorted()
    val e = (knownDigits[6]!! subtract knownDigits[5]!!).first()
    knownDigits[2] = inputSets.first { it.size == 5 && it.contains(e) }.toSet().sorted()
    knownDigits[3] = inputSets.first { it.size == 5 && it != knownDigits[2] && it != knownDigits[5] }.toSet().sorted()
    knownDigits[9] = inputSets.first { it.size == 6 && it != knownDigits[6] && !it.contains(e) }.toSet().sorted()
    knownDigits[0] = inputSets.first { it.size == 6 && it != knownDigits[6] && it != knownDigits[9] }.toSet().sorted()
    val theMap = knownDigits.entries.associate { (i, s) -> s.sorted() to i }
    val mappedOutput = output.map { theMap[it.toSet().sorted()] }
    val parsedOutput = mappedOutput.fold("") { acc, d -> acc + d }.toInt()
    return parsedOutput
}