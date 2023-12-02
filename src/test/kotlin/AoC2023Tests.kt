//region Import challenges
import aoc2023.day1.setupChallenge as day1
import aoc2023.day2.setupChallenge as day2
//endregion

import io.kotest.core.spec.style.FunSpec

class AoC2023Tests: FunSpec({

    test("December 1st")
    {
        day1()
            .withDefaultInput()
            .hasPartOneResult(55538)
            .hasPartTwoResult(54875)
    }

    test("December 2nd")
    {
        day2()
            .withDefaultInput()
            .hasPartOneResult(2268)
            .hasPartTwoResult(63542)
    }
})