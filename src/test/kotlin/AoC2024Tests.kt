//region Import challenges
import aoc2024.day6.setupChallenge as day6
//endregion

import io.kotest.core.spec.style.FunSpec

class AoC2024Tests: FunSpec({

    test("Init")
    {
        day6()
            .withDefaultInput()
            .hasPartOneResult(4967)
    }

    test("December 6th")
    {
        day6()
            .withDefaultInput()
            .hasPartOneResult(4967)
            .hasPartTwoResult(1789)
    }
})