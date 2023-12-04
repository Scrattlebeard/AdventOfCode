//region Import challenges
import aoc2023.day1.setupChallenge as day1
import aoc2023.day2.setupChallenge as day2
import aoc2023.day3.setupChallenge as day3
import aoc2023.day4.setupChallenge as day4
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

    test("December 3rd")
    {
        day3()
            .withDefaultInput()
            .hasPartOneResult(540025)
            .hasPartTwoResult(84584891)
    }

    test("December 4th")
    {
        day4()
            .withDefaultInput()
            .hasPartOneResult(21138)
            .hasPartTwoResult(7185540)
    }
})