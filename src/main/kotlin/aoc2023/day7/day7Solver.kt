package aoc2023.day7

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

data class Hand(val cards: List<Int>, val bid: Long)

fun setupChallenge(): Challenge<List<Hand>> {
    return setup {
        day(7)
        year(2023)

        parser {
            it.readLines()
                .map { it.split(" ") }
                .map { Hand(it[0].getCardValue(), it[1].toLong()) }
        }

        partOne {
            val ranks = it.sortedWith(part1comparator)
            ranks.mapIndexed { index, hand ->
                hand.bid * (index + 1)
            }
                .sum().toString()
        }

        partTwo {
            val ranks = it.sortedWith(part2comparator)
            ranks.mapIndexed { index, hand ->
                hand.bid * (index + 1L)
            }
                .sum().toString()
        }
    }
}

fun String.getCardValue(): List<Int> {
    return this.map {
        when (it) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> it.digitToInt()
        }
    }
}

fun List<Int>.getMatchCounts(): List<Int> {
    val jokers = this.count { it == 1 }
    if(jokers in 1..4) {
        val res = this.filter { it != 1 }.groupBy { it }.values.map { it.size }.sortedDescending().toIntArray()
        res[0] += jokers
        return res.toList()
    }
    return this.groupBy { it }.values.map { it.size }.sortedDescending()
}

fun List<Int>.compareTo(other: List<Int>): Int {
    this.zip(other).forEach {
        if(it.first != it.second) {
            return it.first.compareTo(it.second)
        }
    }
    return this.size.compareTo(other.size)
}

val part1comparator = Comparator<Hand> { a, b ->
    val aCounts = a.cards.getMatchCounts()
    val bCounts = b.cards.getMatchCounts()

    val res = aCounts.compareTo(bCounts)
    if(res == 0) a.cards.compareTo(b.cards) else res
}

val part2comparator = Comparator<Hand> { a, b ->
    val aCards = a.cards.map { if (it == 11) 1 else it }
    val bCards = b.cards.map { if (it == 11) 1 else it }

    val aCounts = aCards.getMatchCounts()
    val bCounts = bCards.getMatchCounts()

    val res = aCounts.compareTo(bCounts)
    if(res == 0) aCards.compareTo(bCards) else res
}