package aoc2023.day2

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

val baseCubeCount = mapOf(Pair("red", 0), Pair("green", 0), Pair("blue", 0))

fun setupChallenge(): Challenge<List<List<List<String>>>> {
    return setup {
        day(2)
        year(2023)

        parser {
            it.readLines()
                .map { it.split(": ").last() }
                .map { it.split("; ").map { it.split(", ") } }
        }

        partOne {
            val limits = mapOf(Pair("red", 12), Pair("green", 13), Pair("blue", 14))
            it.mapIndexed { index, rounds ->
                val cubesPerRound = rounds.map { it.getCubes() }
                val maxCubes =
                    cubesPerRound.fold(baseCubeCount) { count, new -> count.mapValues { entry -> maxOf(entry.value, new[entry.key]!!)} }
                if (maxCubes.all { limits[it.key]!! >= it.value }) index + 1 else 0
            }
                .sum()
                .toString()
        }

        partTwo {
            it.map { rounds ->
                val cubesPerRound = rounds.map { it.getCubes() }
                val maxCubes =
                    cubesPerRound.fold(baseCubeCount) { count, new -> count.mapValues { entry -> maxOf(entry.value, new[entry.key]!!)} }
                maxCubes.values.fold(1L) { n, i -> n * i }
            }
                .sum()
                .toString()
        }
    }
}

fun List<String>.getCubes(): Map<String, Int> {
    val res = baseCubeCount.toMutableMap()
    this.forEach {
        val parts = it.split(" ")
        val number = parts[0].toInt()
        val colour = parts[1]
        if (number > res[colour]!!) {
            res[colour] = number
        }
    }
    return res
}