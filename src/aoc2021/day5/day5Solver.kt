package aoc2021.day5

import lib.*

suspend fun main() {
    challenge<List<LineSegment>> {

        day(5)

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

        solver {
            println("Solving challenge 1...")
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

        solver {
            println("Solving challenge 2...")
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