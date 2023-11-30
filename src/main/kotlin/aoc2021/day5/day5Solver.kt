package aoc2021.day5

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<LineSegment>> {
    return setup {
        day(5)
        year(2021)

        input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .map { line -> line.split(" -> ") }
                .map { points -> Pair(points[0], points[1]) }
                .map { pairs -> Pair(pairs.first.split(','), pairs.second.split(',')) }
                .map { pairs ->
                    Pair(
                        pairs.first[0].toInt() to pairs.first[1].toInt(),
                        pairs.second[0].toInt() to pairs.second[1].toInt()
                    )
                }
                .map { pairs -> LineSegment(pairs) }
        }

        partOne {
            val dict = mutableMapOf<Pair<Int, Int>, Int>()
            it.filter { ls -> ls.start.first == ls.end.first || ls.start.second == ls.end.second }
                .forEach { ls ->
                    ls.getPoints()
                        .forEach { pair ->
                            dict[pair] = dict.getOrDefault(pair, 0) + 1
                        }
                }
            dict.count { pair -> pair.value > 1 }.toString()
        }

        partTwo {
            val dict = mutableMapOf<Pair<Int, Int>, Int>()
            it.forEach { ls ->
                ls.getPoints()
                    .forEach { pair ->
                        dict[pair] = dict.getOrDefault(pair, 0) + 1
                    }
            }
            dict.count { pair -> pair.value > 1 }.toString()
        }
    }
}