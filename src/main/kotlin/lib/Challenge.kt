package lib

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import kotlin.system.measureTimeMillis

@DslMarker
annotation class AocDsl

class Challenge<T> {

    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard!!

    var year = 2022
    var day = 1

    var inputs = listOf<String>()

    lateinit var parser: suspend (File) -> T
    var solvers = mutableListOf<suspend (T) -> String?>()
    var partOneSolvers = mutableListOf<suspend (T) -> String?>()
    var partTwoSolvers = mutableListOf<suspend (T) -> String?>()

    suspend fun solveChallenge() {
        if (inputs.isEmpty()) {
            inputs = listOf("input.txt")
        }

        for (inp in inputs) {
            for (solver in partOneSolvers) {
                println("Solving $inp part one...")
                runSolver(solver, inp)
            }
            for (solver in partTwoSolvers) {
                println("Solving $inp part two...")
                runSolver(solver, inp)
            }
        }
    }

    suspend fun runSolver(solver: (suspend (T) -> String?), inp: String) {
        var input: T
        var elapsed = measureTimeMillis {
            input = parseInput(inp)
        }
        println("Time spent parsing input: $elapsed ms")

        var res: String?
        elapsed = measureTimeMillis {
            res = solver(input)
        }
        if(res != null) {
            clipboard.setContents(StringSelection(res), null)
        }
        println("Time spent: $elapsed ms")
        println("Result: $res\n")
    }

    suspend fun parseInput(name: String = "input.txt"): T {
        return parser(openInput(name))
    }

    private suspend fun openInput(name: String): File {
        val f = File("src/main/kotlin/aoc$year/day$day/$name")
        if (!f.isFile) {
            InputDownloader.instance.createInputFile(day, year)
        }
        return f
    }

    @AocDsl
    fun day(d: Int) {
        this.day = d
    }

    @AocDsl
    fun year(y: Int) {
        this.year = y
    }

    @AocDsl
    fun input(inp: String) {
        inputs = inputs + inp
    }

    @AocDsl
    fun parser(block: suspend (File) -> T) {
        parser = block
    }

    @AocDsl
    fun partOne(block: suspend (T) -> String?) {
        partOneSolvers += block
        solvers += block
    }

    @AocDsl
    fun partTwo(block: suspend (T) -> String?) {
        partTwoSolvers += block
        solvers += block
    }
}

@AocDsl
suspend fun <T> challenge(block: Challenge<T>.() -> Unit) {
    val model = Challenge<T>()
    model.apply(block).solveChallenge()
}

@AocDsl
inline fun <reified T> setup(block: Challenge<T>.() -> Unit): Challenge<T> {
    val model = Challenge<T>()
    return model.apply(block)
}