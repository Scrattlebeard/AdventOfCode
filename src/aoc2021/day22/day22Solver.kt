package aoc2021.day22

import lib.*

suspend fun main() {
    challenge<List<Pair<Cube, Boolean>>> {

        day(22)
        year(2021)

        //input("example3.txt")

        parser {
            val regex = """(on|off) x=(-?\d+)\.\.(-?\d+),y=(-?\d+)\.\.(-?\d+),z=(-?\d+)\.\.(-?\d+)""".toRegex()
            it.readLines().map { regex.find(it)!!.destructured }.map { (b, x1, x2, y1, y2, z1, z2) ->
                Cube(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt(), z1.toInt()..z2.toInt()) to (b == "on")
            }
        }

        solver {
            println("Solving challenge 1...")
            val limit = Cube(-50..50, -50..50, -50..50)
            val relevantCuboids = it.map { it.first intersect limit to it.second }.filter { it.first.isNotEmpty() }
            var activated = listOf<Cube>()
            relevantCuboids.forEach { activation ->
                activated = if (activation.second) {
                    activated.plus(activation.first)
                } else {
                    activated.flatMap { activatedCube -> activatedCube - activation.first }
                }

                var separated = false
                while (!separated) {
                    separated = true
                    activated.forEachIndexed { i, cube ->
                        for(j in i+1 until activated.size) {
                            if(cube intersects activated[j]) {
                                val intersection = cube intersect activated[j]
                                activated = activated
                                    .asSequence()
                                    .minus(setOf(cube, activated[j]))
                                    .plus((cube - intersection).plus(activated[j] - intersection).plus(intersection))
                                    .toSet()
                                    .toList()
                                separated = false
                                break
                            }
                        }
                    }
                }
            }
            activated.sumOf { it.size() }.toString()
        }

        solver {
            println("Solving challenge 2...")
            //441973249 too low
            var activated = listOf<Cube>()
            it.forEach { activation ->
                activated = if (activation.second) {
                    activated.plus(activation.first)
                } else {
                    activated.flatMap { activatedCube -> activatedCube - activation.first }
                }

                var separated = false
                while (!separated) {
                    separated = true
                    activated.forEachIndexed { i, cube ->
                        for(j in i+1 until activated.size) {
                            if(cube intersects activated[j]) {
                                val intersection = cube intersect activated[j]
                                activated = activated
                                    .asSequence()
                                    .minus(setOf(cube, activated[j]))
                                    .plus((cube - intersection).plus(activated[j] - intersection).plus(intersection))
                                    .toSet()
                                    .toList()
                                separated = false
                                break
                            }
                        }
                    }
                }
            }
            activated.sumOf { it.size() }.toString()
        }
    }
}