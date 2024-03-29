package aoc2021.day2

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Pair<String, Int>>> {
    return setup {
        day(2)
        year(2021)

        parser {
            it.readLines()
                .map { s -> s.split(' ') }
                .map { (dir, n) -> Pair(dir, n.toInt()) }
        }

        partOne {
            println("Solving challenge 1...")
            var (pos, depth) = 0 to 0
            it.forEach { (dir, n) ->
                run {
                    when (dir) {
                        "forward" -> pos += n
                        "down" -> depth += n
                        "up" -> depth -= n
                    }
                }
            }
            (pos * depth).toString()
        }

        partTwo {
            println("Solving challenge 2...")
            var (pos, depth, aim) = Triple(0, 0, 0)
            it.forEach { (dir, n) ->
                run {
                    when (dir) {
                        "forward" -> run {
                            pos += n
                            depth += n * aim
                        }

                        "down" -> aim += n
                        "up" -> aim -= n
                    }
                }
            }
            (pos * depth).toString()
        }
    }
}