package aoc2024.day2

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<List<Long>>> {
    return setup {
        day(2)
        year(2024)

        parser {
            it.readLines()
                .map { it.asListOfLongs() }
        }

        partOne {
            it.count { it.isValid() }.toString()
        }

        partTwo {
            it.count { it.isValidWithDampener() }.toString()
        }
    }
}

fun List<Long>.isValidDescending() : Boolean {
    var i = 1
    while(i < this.size) {
        val diff = this[i-1] - this[i]
        if(diff < 1 || diff > 3) {
            return false
        }
        i++
    }
    return true
}

fun List<Long>.isValidAscending() : Boolean {
    var i = 1
    while(i < this.size) {
        val diff = this[i-1] - this[i]
        if(diff < -3 || diff > -1) {
            return false
        }
        i++
    }
    return true
}

fun List<Long>.isValid() : Boolean {
    return this.isValidAscending() || this.isValidDescending()
}

fun List<Long>.isValidWithDampener() : Boolean {
    return this.isValid()
            || this.indices.any { this.filterIndexed { index, _ -> index != it }.isValid() }
}
