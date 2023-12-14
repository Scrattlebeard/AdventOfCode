package aoc2023.day14

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(14)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines().get2DArrayOfColumns()
        }

        partOne {
            val tilted = it.tilt(CardinalDirection.North)
            tilted.calculateLoad().toString()
        }

        partTwo {
            var tilted = it
            val history = mutableMapOf<String, Pair<Int, Array<Array<Char>>>>()
            val limit = 1000000000
            var i = 0
            while(i < limit) {
                val key = tilted.asString()
                if(history.containsKey(key)) {
                    val rep = history[key]!!
                    val repLength = i - rep.first
                    val ff = (limit - i) / (repLength)
                    println("Fast-forwarding from $i for $ff * $repLength reps")
                    i += (ff * repLength)
                    tilted = rep.second
                }
                tilted = tilted.spin()
                history[key] = i to tilted
                i++
            }
            tilted.calculateLoad().toString()
        }
    }
}

fun Array<Array<Char>>.tilt(direction: CardinalDirection): Array<Array<Char>> {
    val tilted = this.clone()

    val ranges = when(direction) {
        CardinalDirection.North -> this.indices to this.first().indices
        CardinalDirection.South -> this.indices to this.first().indices.reversed()
        CardinalDirection.West -> this.indices to this.first().indices
        CardinalDirection.East -> this.indices.reversed() to this.first().indices
    }

    ranges.first.forEach { i ->
        ranges.second.forEach { j ->
            if (this[i][j] == 'O') {
                var pos = i to j
                var blocked = false
                while (!blocked) {
                    val newPos = pos.adjacent(direction)
                    if (isValidCoord(newPos.x(), newPos.y()) && tilted[newPos.x()][newPos.y()] == '.') {
                        pos = newPos
                    } else {
                        blocked = true
                    }
                }
                tilted[i][j] = '.'
                tilted[pos.x()][pos.y()] = 'O'
            }
        }
    }

    return tilted
}

fun Array<Array<Char>>.calculateLoad(): Long {
    return this.first().indices.map { i ->
        this.map { it[i] }
            .count { it == 'O' } * (this.size - i)
    }
        .sumOf { it.toLong() }
}

fun Array<Array<Char>>.asString(): String {
    return this.fold("") {s, r ->
        s + r.fold("") {s2, c ->
            s2 + c
        }
    }
}
fun Array<Array<Char>>.spin(): Array<Array<Char>> {
    var tilted = this
    tilted = tilted.tilt(CardinalDirection.North)
    tilted = tilted.tilt(CardinalDirection.West)
    tilted = tilted.tilt(CardinalDirection.South)
    tilted = tilted.tilt(CardinalDirection.East)
    return tilted
}