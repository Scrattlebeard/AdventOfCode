//region Import challenges
import aoc2023.day1.setupChallenge as day1
import aoc2023.day2.setupChallenge as day2
import aoc2023.day3.setupChallenge as day3
import aoc2023.day4.setupChallenge as day4
import aoc2023.day5.setupChallenge as day5
import aoc2023.day6.setupChallenge as day6
import aoc2023.day7.setupChallenge as day7
import aoc2023.day8.setupChallenge as day8
import aoc2023.day9.setupChallenge as day9
import aoc2023.day10.setupChallenge as day10
import aoc2023.day11.setupChallenge as day11
import aoc2023.day12.setupChallenge as day12
import aoc2023.day13.setupChallenge as day13
//endregion

import io.kotest.core.spec.style.FunSpec

class AoC2023Tests: FunSpec({

    test("Init")
    {
        day1()
            .withExampleInput()
            .hasPartOneResult(142)
    }

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

    test("December 5th")
    {
        day5()
            .withDefaultInput()
            .hasPartOneResult(484023871)
            .hasPartTwoResult(46294175)
    }

    test("December 6th")
    {
        day6()
            .withDefaultInput()
            .hasPartOneResult(1710720)
            .hasPartTwoResult(35349468)
    }

    test("December 7th")
    {
        day7()
            .withDefaultInput()
            .hasPartOneResult(246424613)
            .hasPartTwoResult(248256639)
    }

    test("December 8th")
    {
        day8()
            .withDefaultInput()
            .hasPartOneResult(20777)
            .hasPartTwoResult(13289612809129)
    }

    test("December 9th")
    {
        day9()
            .withDefaultInput()
            .hasPartOneResult(2075724761)
            .hasPartTwoResult(1072)
    }

    test("December 10th")
    {
        day10()
            .withDefaultInput()
            .hasPartOneResult(7102)
            .hasPartTwoResult(363)
    }

    test("December 11th")
    {
        day11()
            .withDefaultInput()
            .hasPartOneResult(9591768)
            .hasPartTwoResult(746962097860)
    }

    test("December 12th")
    {
        day12()
            .withDefaultInput()
            .hasPartOneResult(7622)
            .hasPartTwoResult(4964259839627)
    }

    test("December 13th")
    {
        day13()
            .withDefaultInput()
            .hasPartOneResult(37561)
            .hasPartTwoResult(31108)
    }
})