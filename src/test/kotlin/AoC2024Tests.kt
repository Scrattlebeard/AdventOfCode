//region Import challenges
import aoc2024.day1.setupChallenge as day1
import aoc2024.day2.setupChallenge as day2
import aoc2024.day6.setupChallenge as day6
import aoc2024.day7.setupChallenge as day7
import aoc2024.day8.setupChallenge as day8
//endregion

import io.kotest.core.spec.style.FunSpec

class AoC2024Tests: FunSpec({

    test("Init")
    {
        day6()
            .withDefaultInput()
            .hasPartOneResult(4967)
    }

    test("December 1st")
    {
        day1()
            .withDefaultInput()
            .hasPartOneResult(2192892)
            .hasPartTwoResult(22962826)
    }

    test("December 2nc")
    {
        day2()
            .withDefaultInput()
            .hasPartOneResult(663)
            .hasPartTwoResult(692)
    }

    test("December 6th")
    {
        day6()
            .withDefaultInput()
            .hasPartOneResult(4967)
            .hasPartTwoResult(1789)
    }

    test("December 7th")
    {
        day7()
            .withDefaultInput()
            .hasPartOneResult(14711933466277)
            .hasPartTwoResult(286580387663654)
    }

    test("December 8th")
    {
        day8()
            .withDefaultInput()
            .hasPartOneResult(327)
            .hasPartTwoResult(1233)
    }
})