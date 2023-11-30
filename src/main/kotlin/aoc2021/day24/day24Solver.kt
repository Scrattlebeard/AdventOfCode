package aoc2021.day24

import lib.*
import java.io.File
import java.lang.StringBuilder

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Triple<String, Char, String>>> {
    return setup {
        day(24)
        year(2021)

        //input("bin_example.txt")

        parser {
            val prog = it.readLines()
                .map { it.split(" ") }
                .map { Triple(it[0], it[1].first(), it.getOrElse(2) { "0" }) }
            ALUTranspiler().transpile(prog)
            prog
        }

        partOne {
            //val alu = ALU(it)
            var best = listOf<Int>()
            for (i in ModelNumbers()) {
                //for(i in listOf(listOf(1), listOf(0), listOf(15), listOf(4), listOf(2))) {
                val res = ALUTemplate().run(listOf(4, 1, 1, 7, 1, 1, 8, 3, 1, 4, 1, 2, 9, 1))
                if (res == 0) {
                    best = i
                    break
                }
                //alu.reset()
            }
            best.toString()
        }

        partTwo {
            "Todo challenge 2"
        }
    }
}

class ALU(val program: List<Triple<String, Char, String>>) {
    var state = listOf('w', 'x', 'y', 'z').associateWith { 0 }.toMutableMap()

    fun runProgram(inputBuffer: List<Int>): Int {
        var inputIndex = 0
        program.forEach {
            val dest = it.second
            val value = it.third.toIntOrNull() ?: state[it.third.first()]!!
            state[dest] = when (it.first) {
                "inp" -> {
                    val read = inputBuffer[inputIndex]
                    inputIndex++
                    read
                }

                "add" -> {
                    state[dest]!! + value
                }

                "mul" -> {
                    state[dest]!! * value
                }

                "div" -> {
                    state[dest]!! / value
                }

                "mod" -> {
                    state[dest]!! % value
                }

                "eql" -> {
                    if (state[dest]!! == value) 1 else 0
                }

                else -> throw Exception("Unknown instruction: ${it.first}")
            }
        }
        return state['z']!!
    }

    fun reset() {
        state = state.map { (k, _) -> k to 0 }.toMap().toMutableMap()
    }
}

class ModelNumbers : Iterator<List<Int>> {
    //99999717524234 too high
    //91288299697996 too low
    val state = mutableListOf(9, 9, 6, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9)
    var toDecrement = 13

    override fun hasNext(): Boolean {
        return state.any { it > 1 }
    }

    override fun next(): List<Int> {
        state[toDecrement]--
        if (state[toDecrement] == 0) {
            while (state[toDecrement] == 0) {
                state[toDecrement] = 9
                toDecrement--
                state[toDecrement]--
            }
            if (toDecrement <= 4) {
                println("Reached: $state")
            }
            toDecrement = 13
        }
        return state
    }
}

class ALUTranspiler() {
    var inputIndex = 0

    fun transpile(program: List<Triple<String, Char, String>>) {

        val content = StringBuilder()

        program.forEach {
            val dest = it.second
            val value = it.third
            content.appendLine(
                when (it.first) {
                    "inp" -> {
                        val line = "$dest = input[$inputIndex]"
                        inputIndex++
                        line
                    }

                    "add" -> {
                        if (value != "0") {
                            "$dest += $value"
                        } else {
                            ""
                        }
                    }

                    "mul" -> {
                        when (value) {
                            "0" -> "$dest = 0"
                            "1" -> ""
                            else -> "$dest *= $value"
                        }
                    }

                    "div" -> {
                        if (value != "1") {
                            "$dest /= $value"
                        } else {
                            ""
                        }
                    }

                    "mod" -> {
                        if (value != "1") {
                            "$dest %= $value"
                        } else {
                            ""
                        }
                    }

                    "eql" -> {
                        "$dest = if($dest == $value) 1 else 0"
                    }

                    else -> throw Exception("Unknown instruction: ${it.first}")
                }
            )
        }

        val template = File("src/aoc2021/day24/ALUTemplate.txt").readText()
        val f = File("src/aoc2021/day24/ALU.kt")
        f.writeText(template.replace("//<program here>", content.toString()))
    }

    fun transpile2() {

    }
}

/*
[2] = 1 => 13
[3] = 13 - 6 = 7

[5] = 1 => 17
[6] = 17 - 9 = 8


[8] = 1 => 9
[9] = 9 - 5 = 4

[7] = 3 => 10
[10] = 10 - 9 = 1

[4] = 1 => 7
[11] = 7 - 5 = 2

[1] = 1 => 11
[12] = 11 - 2 = 9

[0] = 4 => 8
[13] = 8 - 7 = 1
 */