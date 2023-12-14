package aoc2023.day12

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Pair<String, List<Int>>>> {
    return setup {
        day(12)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines()
                .map { it.split(" ") }
                .map { it[0] to it[1].asListOfInts(",") }
        }

        partOne {
            it.sumOf { it.first.replace('.', ' ').countValidCombinations(it.second) }
                .toString()
        }

        partTwo {
            val unfurled = it.map { it.unfurl() }
            unfurled.sumOf { it.first.replace('.', ' ').countValidCombinations(it.second) }
                .toString()
        }
    }
}

fun Pair<String, List<Int>>.unfurl(): Pair<String, List<Int>> {
    val newString = StringBuilder(this.first)
    repeat(4) {
        newString.append('?')
        newString.append(this.first)
    }
    return newString.toString() to List(5) { this.second }.flatten()
}

fun String.countValidCombinations(numDamagedSprings: List<Int>): Long {
    val damagedSpringsToFind = numDamagedSprings.sum() - this.count { it == '#' }
    val res = this.countValidPlacements(numDamagedSprings, damagedSpringsToFind)
    //println("Found $res solutions...")
    return res!!
}

val solutions = mutableMapOf<String, Long?>()

fun String.countValidPlacements(groupSizes: List<Int>, toPlace: Int): Long? {

    val myKey = groupSizes.fold(this) {a, i -> "$a,$i" }
    if(solutions.containsKey(myKey)) {
        return solutions[myKey]
    }

    val targetGroupNumber = groupSizes.size
    val groups = this.split(" ").filter { it.isNotEmpty() }
    val groupPotential = this.count { it == '?' } - toPlace

    if (groups.size + groupPotential < targetGroupNumber) {
        return null
    }

    if (toPlace == 0) {
        val isValid = this.replace('?', ' ').verify(groupSizes)
        return if (isValid) 1 else null
    }

    val placements = mutableListOf<Pair<String, List<Int>>>()
    var i = 0
    while (i < this.length) {
        if (this[i] == '?') {

            val prefix = this.substring(0 until i).replace('?', ' ') + "#"

            val groupsMatched = prefix.verifyPrefix(groupSizes)
            if(groupsMatched < 0) {
                break
            }

            val candidate = prefix.split(" ").last { it.isNotEmpty() } + this.substring(minOf(i + 1, this.length) until this.length)
            val verificationIndex = candidate.indexOf(' ')
            val toVerify = if (verificationIndex > 0) candidate.substring(0, verificationIndex) else candidate
            if (toVerify.verifyCurrentGroup(groupSizes[groupsMatched])) {
                placements.add(candidate to groupSizes.drop(groupsMatched))
            }
        }
        i++
    }

    var sum = 0L
    placements.forEach{
        val res = it.first.countValidPlacements(it.second, toPlace - 1)
        val key = it.second.fold(it.first) {a, i -> "$a,$i" }
        solutions[key] = res
        sum += res ?: 0L
    }

    return sum
}

fun String.verifyPrefix(groupSizes: List<Int>): Int {
    val groups = this.split(" ").filter { it.isNotEmpty() }.dropLast(1)

    if (groups.isEmpty()) {
        return 0
    }

    if (groups.size > groupSizes.size) {
        return -1
    }

    groups.forEachIndexed { index, s ->
        if (s.length != groupSizes[index]) {
            return -1
        }
    }

    return groups.size
}

fun String.verifyCurrentGroup(targetSize: Int): Boolean {
    val groups = this.split(" ").filter { it.isNotEmpty() }
    val group = groups.last()

    if (group.contains('#')) {
        if (group.length < targetSize || group.split("?").first().length > targetSize) {
            return false
        }
    }

    return true
}

fun String.verify(groupSizes: List<Int>): Boolean {
    val groups = this.split(" ").filter { it.isNotEmpty() }.map { it.length }
    return groups.zip(groupSizes).all { it.first == it.second }
}