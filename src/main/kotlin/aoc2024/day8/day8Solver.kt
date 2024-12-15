package aoc2024.day8

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Array<Array<Char>>> {
    return setup {
        day(8)
        year(2024)

        parser {
            it.readLines()
                .get2DArrayOfColumns()
        }

        partOne {
            val antennas = it.findAntennas()
            val antiNodes = antennas.values.flatMap { it.findAntinodes() }
            antiNodes.toSet().count { antinode -> it.isValidCoord(antinode) }.toString()
        }

        partTwo { map ->
            val antennas = map.findAntennas()
            val antiNodes = antennas.values.flatMap { it.findRepeatingAntinodes(map) }
            antiNodes.toSet().count().toString()
        }
    }
}

data class Antenna(val pos: Pair<Int, Int>, val freq: Char) {

    fun findAntinode(other: Antenna) : Pair<Int, Int> {
        return this.pos.findAntinode(other.pos)
    }

    fun findRepeatingAntinodes(other: Antenna, map: Array<Array<Char>>) : List<Pair<Int, Int>> {
        val res = mutableListOf(this.pos, other.pos)
        var a = other.pos
        var b = this.pos
        var c = b.findAntinode(a)
        while(map.isValidCoord(c)) {
            res.add(c)
            a = b
            b = c
            c = b.findAntinode(a)
        }
        return res
    }
}

fun Pair<Int, Int>.findAntinode(other: Pair<Int, Int>) : Pair<Int, Int> {
    val relPos = this - other
    return this.first + relPos.first to this.second + relPos.second
}

fun Array<Array<Char>>.findAntennas() : Map<Char, List<Antenna>> {
    return this.flatMapIndexed { x, column -> column.mapIndexed { y, row -> Antenna(x to y, this[x][y])  }  }
        .filter { it.freq != '.' }
        .groupBy { it.freq }
}

fun List<Antenna>.findAntinodes() : List<Pair<Int, Int>> {
    return this.flatMap { antenna -> this.filter { it.pos != antenna.pos }.map { antenna.findAntinode(it) } }
}

fun List<Antenna>.findRepeatingAntinodes(map: Array<Array<Char>>) : List<Pair<Int, Int>> {
    return this.flatMap { antenna -> this.filter { it.pos != antenna.pos }.flatMap { antenna.findRepeatingAntinodes(it, map) } }
}