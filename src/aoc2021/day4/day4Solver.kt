package aoc2021.day4

import lib.*

suspend fun main() {
    challenge<Pair<List<Int>, List<Board>>> {

        day(4)
        year(2021)

        input("example.txt")
        input("input.txt")

        parser {
            val splitInput = it.getStringsGroupedByEmptyLine()
            val numbers = splitInput.first().first().split(',').map { s -> s.toInt() }
            val boards = readBoards(splitInput.slice(1 until splitInput.size))
            numbers to boards
        }

        solver {
            println("Solving challenge 1...")
            val numbers = it.first
            val boards = it.second
            var i = 0
            var score = -1L
            while(i < numbers.size) {
                boards.map{b -> b.draw(numbers[i])}
                val winner = boards.firstOrNull { b -> b.score > -1 }
                if(winner != null) {
                    score = winner.score
                    break
                }
                i++
            }
            score.toString()
        }

        solver {
            println("Solving challenge 2...")
            val numbers = it.first
            var boards = it.second
            var i = 0
            var score = -1L
            while(boards.isNotEmpty()) {
                boards.map{b -> b.draw(numbers[i])}
                val winners = boards.filter { b -> b.score > -1 }
                boards = boards.filter { b -> b.score == -1L }
                if(boards.isEmpty()) {
                    score = winners.first().calculateScore(numbers[i].toLong())
                    break
                }
                i++
            }
            score.toString()
        }
    }
}

fun readBoards(input: List<List<String>>): List<Board> {
    return input
        .map {
            Board(it
                .map { s ->
                    s.split(' ').filter{it.isNotEmpty()}
                        .map { it.toInt() }
                })
        }
}