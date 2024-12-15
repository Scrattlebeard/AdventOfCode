//region Import challenges
import aoc2024.day1.setupChallenge as day1
import aoc2024.day2.setupChallenge as day2
import aoc2024.day3.setupChallenge as day3
import aoc2024.day5.setupChallenge as day5
import aoc2024.day6.setupChallenge as day6
import aoc2024.day7.setupChallenge as day7
import aoc2024.day8.setupChallenge as day8
import aoc2024.day9.setupChallenge as day9
import aoc2024.day10.setupChallenge as day10
import aoc2024.day11.setupChallenge as day11
import aoc2024.day12.setupChallenge as day12
import aoc2024.day13.setupChallenge as day13
import aoc2024.day14.setupChallenge as day14
import aoc2024.day15.setupChallenge as day15
//endregion

import io.kotest.core.spec.style.FunSpec

class AoC2024Tests: FunSpec({

    test("Init")
    {
        day2()
            .withDefaultInput()
            .hasPartOneResult(663)
    }

    test("December 1st")
    {
        day1()
            .withDefaultInput()
            .hasPartOneResult(2192892)
            .hasPartTwoResult(22962826)
    }

    test("December 2nd")
    {
        day2()
            .withDefaultInput()
            .hasPartOneResult(663)
            .hasPartTwoResult(692)
    }

    test("December 3rd")
    {
        day3()
            .withDefaultInput()
            .hasPartOneResult(159892596)
            .hasPartTwoResult(92626942)
    }

    test("December 5th")
    {
        day5()
            .withDefaultInput()
            .hasPartOneResult(5166)
            .hasPartTwoResult(4679)
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

    test("December 9th")
    {
        day9()
            .withDefaultInput()
            .hasPartOneResult(6332189866718)
    }

    test("December 10th")
    {
        day10()
            .withDefaultInput()
            .hasPartOneResult(638)
            .hasPartTwoResult(1289)
    }

    test("December 11th")
    {
        day11()
            .withDefaultInput()
            .hasPartOneResult(203228)
    }

    test("December 12th")
    {
        day12()
            .withDefaultInput()
            .hasPartOneResult(1396298)
    }

    test("December 14th")
    {
        day14()
            .withDefaultInput()
            .hasPartOneResult(229632480)
    }

    test("December 15th")
    {
        day15()
            .withDefaultInput()
            .hasPartOneResult(1463512)
            .hasPartTwoResult(1486520)
    }
})