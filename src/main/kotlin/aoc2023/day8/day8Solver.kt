package aoc2023.day8

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

data class Desert(val instructions: String, val map: Map<String, Pair<String, String>>)

fun setupChallenge(): Challenge<Desert> {
    return setup {
        day(8)
        year(2023)

        parser {
            val parts = it.getStringsGroupedByEmptyLine()
            val map = parts[1].associate {
                it.substring(0..2) to (it.substring(7..9) to it.substring(12..14))
            }
            Desert(parts[0][0], map)
        }

        partOne {
            var current = "AAA"
            var i = 0
            while(current != "ZZZ") {
                current = if(it.instructions[i % it.instructions.length] == 'L') it.map[current]!!.first else it.map[current]!!.second
                i++
            }
            i.toString()
        }

        partTwo {
            val sources = it.map.keys.filter { it.last() == 'A' }
            val sourceMaps = sources.map { source -> it.buildPath(source) }
            leastCommonMultiple(sourceMaps.map { it.size.toLong()}).toString()
        }
    }
}

fun Desert.buildPath(source: String): List<String> {
    var current = source
    var i = 0
    while(current.last() != 'Z') {
        current = if(this.instructions[i % this.instructions.length] == 'L') this.map[current]!!.first else this.map[current]!!.second
        i++
    }
    val res = mutableListOf<String>()
    do {
        current = if(this.instructions[i % this.instructions.length] == 'L') this.map[current]!!.first else this.map[current]!!.second
        i++
        res.add(current)
    } while (current.last() != 'Z')
    return res
}