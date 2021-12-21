package aoc2021.day19

import lib.*

suspend fun main() {
    challenge<List<List<Triple<Int, Int, Int>>>> {

        day(19)

        parser {
            it.getStringsGroupedByEmptyLine()
                .map { it.subList(1, it.size - 1) }
                .map { it.map { it.split(",").map { it.toInt() } } }
                .map { it.map { Triple(it[0], it[1], it[2]) } }
        }

        solver {
            println("Solving challenge 1...")
            //Use first group as ground truth for coordinate system
            var truth = it.first().sortedBy { it.first }

            //Sort by each axis, try to match sliding size 12 window?
            //Won't work, since the relative 0 differs. Need to look at pointwise relative distances.
            for(i in it.indices) {
                for(j in i+1 until it.size) {
                    var iz = it[i].getAllOrientations()
                    var js = it[j].getAllOrientations()
                    iz.forEach{ l ->
                        js.forEach { if(it.tryMatch(l)) println("Found!")  }
                    }
                }
            }
            it.subList(1, it.size).forEach {
                it.getAllOrientations().forEach {
                    val res = it.tryMatch(truth)
                    if (res) println("Found!")
                }
            }

            //println(listOf(Triple(-1, -1, 1), Triple(8, 0, 7)).getAllOrientations())

            "Todo challenge 1"
        }

        solver {
            println("Solving challenge 2...")
            "Todo challenge 2"
        }
    }
}

fun List<Triple<Int, Int, Int>>.tryMatch(truth: List<Triple<Int, Int, Int>>): Boolean {
    val truthWindows = truth.sortedBy { it.first }.windowed(12)

    val repr = this.sortedBy { it.first }
    val windows = repr.windowed(12)

    val matches = windows.map { w -> truthWindows.any { w == it } }
    return matches.any { it }
}

fun List<Triple<Int, Int, Int>>.getAllOrientations(): List<List<Triple<Int, Int, Int>>> {
    val res = this.map {
        listOf(
            //Facing x
            Triple(it.first, it.second, it.third),
            Triple(it.first, -it.second, -it.third),
            Triple(it.first, it.third, -it.second),
            Triple(it.first, -it.third, it.second),
            //Facing -x
            Triple(-it.first, -it.second, it.third),
            Triple(-it.first, it.second, -it.third),
            Triple(-it.first, -it.third, -it.second),
            Triple(-it.first, it.third, it.second),
            //Facing y
            Triple(it.second, -it.first, it.third),
            Triple(it.second, it.first, -it.third),
            Triple(it.second, it.third, -it.first),
            Triple(it.second, -it.third, -it.first),
            //Facing -y
            Triple(-it.second, it.first, it.third),
            Triple(-it.second, it.third, -it.first),
            Triple(-it.second, -it.third, it.first),
            Triple(-it.second, -it.first, -it.third),
            //Facing z
            Triple(it.third, it.second, -it.first),
            Triple(it.third, it.first, it.second),
            Triple(it.third, -it.second, it.first),
            Triple(it.third, -it.first, -it.second),
            //Facing -z
            Triple(-it.third, it.second, it.first),
            Triple(-it.third, it.first, -it.second),
            Triple(-it.third, -it.second, -it.first),
            Triple(-it.third, -it.first, it.second)
        )
    }
    return List(24){i -> res.map{it[i]}.sortedBy { it.first }}
}