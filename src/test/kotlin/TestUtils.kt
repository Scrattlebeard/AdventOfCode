import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import lib.Challenge

fun <T> Challenge<T>.withDefaultInput(): Challenge<T> {
    this.inputs = listOf("input.txt")
    return this
}

fun <T> Challenge<T>.withExampleInput(): Challenge<T> {
    this.inputs = listOf("example.txt")
    return this
}

suspend fun <T> Challenge<T>.hasPartOneResult(result: String): Challenge<T> {
    this.inputs.forEach {

        val inputName = it

        this.partOneSolvers.forEach {
            withClue("${this.day}/${this.year} part one, $inputName") {
                it.invoke(this.parseInput(inputName)) shouldBe result
            }
        }
    }
    return this
}

suspend fun <T> Challenge<T>.hasPartOneResult(result: Long): Challenge<T> {
    return this.hasPartOneResult("$result")
}

suspend fun <T> Challenge<T>.hasPartTwoResult(result: String): Challenge<T> {
    this.inputs.forEach {

        val inputName = it

        this.partTwoSolvers.forEach {
            withClue("${this.day}/${this.year} part two, $inputName") {
                it.invoke(this.parseInput(inputName)) shouldBe result
            }

        }
    }
    return this
}

suspend fun <T> Challenge<T>.hasPartTwoResult(result: Long): Challenge<T> {
    return this.hasPartTwoResult("$result")
}