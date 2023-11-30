package aoc2021.day21

import lib.*
import kotlin.math.pow
import kotlin.math.roundToLong

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<Int, Int>> {
    return setup {
        day(21)
        year(2021)

        //input("example.txt")

        parser {
            val (p1, p2) = """Player 1 starting position: (\d)\r\nPlayer 2 starting position: (\d)"""
                .toRegex()
                .find(it.readText())!!
                .destructured
            p1.toInt() to p2.toInt()
        }

        partOne {
            val die = DeterministicDie(100)
            var player1 = it.first to 0
            var player2 = it.second to 0
            while (true) {
                player1 = player1.takeTurn(die)
                if (player1.second >= 1000) {
                    break
                }
                player2 = player2.takeTurn(die)
                if (player2.second >= 1000) {
                    break
                }
            }
            (minOf(player1.second, player2.second) * die.rolls).toString()
        }

        partTwo {
            val p1 = countWaysToWin(it.first, 0)
            val p2 = countWaysToWin(it.second, 0)
            var p1wins = 0L
            var p2wins = 0L
            val numTurns = p1.keys union p2.keys
            numTurns.forEach {
                p1wins += p1[it]!! * (27.0.pow(it - 1)
                    .roundToLong() - p2.filterKeys { k -> k < it }.entries.sumOf { (k, v) ->
                    v * 27.0.pow(it - k - 1).roundToLong()
                })
                p2wins += p2[it]!! * (27.0.pow(it)
                    .roundToLong() - p1.filterKeys { k -> k <= it }.entries.sumOf { (k, v) ->
                    v * 27.0.pow(it - k).roundToLong()
                })
            }

            "Winner: ${if (p1wins > p2wins) "P1" else "P2"} with ${maxOf(p1wins, p2wins)}"
        }
    }
}

val diceMap = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)

fun countWaysToWin(pos: Int, points: Int): Map<Int, Long> {
    if (points > 20) {
        return mapOf(0 to 1)
    }
    val waysToWin = mutableMapOf<Int, Long>()
    for (i in 3..9) {
        var newPos = (pos + i) % 10
        if (newPos == 0) {
            newPos = 10
        }

        val res = countWaysToWin(newPos, points + newPos)
        res.forEach {
            waysToWin.upsert(it.key + 1, it.value * diceMap[i]!!, Long::plus)
        }
    }
    return waysToWin
}

fun <K, V> MutableMap<K, V>.upsert(key: K, v: V, updater: (prior: V, new: V) -> V) {
    if (this.containsKey(key)) {
        this[key] = updater(this[key]!!, v)
    } else {
        this[key] = v
    }
}

fun Pair<Int, Int>.takeTurn(die: DeterministicDie): Pair<Int, Int> {
    val roll = die.roll()
    var newPos = (this.first + roll.first + roll.second + roll.third) % 10
    if (newPos == 0) {
        newPos = 10
    }
    val newScore = this.second + newPos
    return newPos to newScore
}

class DeterministicDie(val maxVal: Int) {
    var rolls = 0

    fun roll(): Triple<Int, Int, Int> {
        rolls += 3
        return when (val res = rolls % maxVal) {
            0 -> {
                Triple(maxVal - 2, maxVal - 1, maxVal)
            }

            1 -> {
                Triple(maxVal - 1, maxVal, 1)
            }

            2 -> {
                Triple(maxVal, 1, 2)
            }

            else -> Triple(res - 2, res - 1, res)
        }
    }
}