package aoc2023.day4

import lib.*
import kotlin.math.min

suspend fun main() {
    setupChallenge().solveChallenge()
}

data class Scratchcard(val winningNumbers: Set<Int>, val myNumbers: Set<Int>)

fun setupChallenge(): Challenge<List<Scratchcard>> {
    return setup {
        day(4)
        year(2023)

        parser {
            it.readLines()
                .map { it.split(':')[1].trim() } //Remove "Card n  :" prefix
                .map { it.split("|").map { it.asListofInts() } }
                .map { Scratchcard(it[0].toSet(), it[1].toSet()) }
        }

        partOne {
            it.map { (it.winningNumbers intersect it.myNumbers).size }
                .filter { it > 0 }
                .sumOf { 2.pow(it - 1).toLong() }
                .toString()
        }

        partTwo {
            val cards = it.map { object { val wins = (it.winningNumbers intersect it.myNumbers).size; var count = 1L } }
            cards.indices.forEach {i ->
                val wins = cards[i].wins
                if(wins > 0) {
                    (i+1..min(i + wins, cards.count() - 1)).forEach {
                        cards[it].count += cards[i].count
                    }
                }
            }
            cards.sumOf { it.count }.toString()
        }
    }
}