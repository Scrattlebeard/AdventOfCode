package aoc2024.day5

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<List<Pair<Int, Int>>, List<List<Int>>>> {
    return setup {
        day(5)
        year(2024)

        parser {
            val parts = it.getStringsGroupedByEmptyLine()
            val rules = parts[0].map { it.split("|") }.map { it[0].toInt() to it[1].toInt() }
            val updates = parts[1].map { it.split(",").map { it.toInt() } }
            rules to updates
        }

        partOne {
            it.second
                .filter { update -> it.first.isLegal(update) }
                .sumOf { it.elementAt(it.size / 2) }
                .toString()
        }

        partTwo {
                it.second
                .filter { update -> !it.first.isLegal(update) }
                .map { update -> it.first.filter { it.first in update && it.second in update }.sortPages(update) }
                .sumOf { it.elementAt(it.size / 2) }
                .toString()
        }
    }
}

fun List<Pair<Int, Int>>.isLegal(update: List<Int>): Boolean {
    return this.fold(true) { res, rule -> res && rule.check(update) }
}

fun Pair<Int, Int>.check(update: List<Int>): Boolean {
    if (this.first !in update || this.second !in update) {
        return true
    }
    return update.indexOf(this.first) < update.indexOf(this.second)
}

fun List<Pair<Int, Int>>.sortPages(pages: List<Int>): List<Int> {
    val res = mutableListOf<Int>()
    val remainingPages = pages.toMutableList()
    val remainingRules = this.toMutableList()
    while(remainingRules.any()) {
        val next = remainingPages.first { page -> remainingRules.none { it.second == page } }
        res.add(next)
        remainingPages.remove(next)
        remainingRules.removeAll { it.first == next }
    }
    remainingPages.forEach {
        res.add(it)
    }
    return res
}
