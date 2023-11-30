//region Import Challenges
import aoc2022.day1.setupChallenge as day1
import aoc2022.day2.setupChallenge as day2
import aoc2022.day3.setupChallenge as day3
import aoc2022.day4.setupChallenge as day4
import aoc2022.day5.setupChallenge as day5
import aoc2022.day6.setupChallenge as day6
import aoc2022.day7.setupChallenge as day7
import aoc2022.day8.setupChallenge as day8
import aoc2022.day9.setupChallenge as day9
import aoc2022.day10.setupChallenge as day10
import aoc2022.day11.setupChallenge as day11
import aoc2022.day12.setupChallenge as day12
import aoc2022.day13.setupChallenge as day13
import aoc2022.day14.setupChallenge as day14
import aoc2022.day15.setupChallenge as day15
import aoc2022.day16.setupChallenge as day16
import aoc2022.day17.setupChallenge as day17
import aoc2022.day18.setupChallenge as day18
import aoc2022.day19.setupChallenge as day19
import aoc2022.day20.setupChallenge as day20
import aoc2022.day21.setupChallenge as day21
import aoc2022.day22.setupChallenge as day22
import aoc2022.day23.setupChallenge as day23
import aoc2022.day24.setupChallenge as day24
import aoc2022.day25.setupChallenge as day25
//endregion
import io.kotest.core.spec.style.FunSpec

class AoC2022Tests : FunSpec({

    test("December 1st")
    {
        day1()
            .withDefaultInput()
            .hasPartOneResult(68923)
            .hasPartTwoResult(200044)
    }

    test("December 2nd")
    {
        day2()
            .withDefaultInput()
            .hasPartOneResult(8392)
            .hasPartTwoResult(10116)
    }

    test("December 3rd")
    {
        day3()
            .withDefaultInput()
            .hasPartOneResult(8515)
            .hasPartTwoResult(2434)
    }

    test("December 4th") {
        day4()
            .withDefaultInput()
            .hasPartOneResult(538)
            .hasPartTwoResult(792)
    }

    test("December 5th") {
        day5()
            .withDefaultInput()
            .hasPartOneResult("NTWZZWHFV")
            .hasPartTwoResult("BRZGFVBTJ")
    }

    test("December 6th") {
        day6()
            .withDefaultInput()
            .hasPartOneResult(1625)
            .hasPartTwoResult(2250)
    }

    test("December 7th") {
        day7()
            .withExampleInput()
            .hasPartOneResult(95437)
            .hasPartTwoResult(24933642)
            .withDefaultInput()
            .hasPartOneResult(1390824)
            .hasPartTwoResult(7490863)
    }

    test("December 8th") {
        day8()
            .withDefaultInput()
            .hasPartOneResult(1812)
            .hasPartTwoResult(315495)
    }

    test("December 9th") {
        day9()
            .withDefaultInput()
            .hasPartOneResult(6181)
            .hasPartTwoResult(2386)
    }

    test("December 10th") {
        val part2result = "\n" +
                "###....##.####.###..###..####.####..##..\n" +
                "#..#....#.#....#..#.#..#.#....#....#..#.\n" +
                "#..#....#.###..#..#.#..#.###..###..#....\n" +
                "###.....#.#....###..###..#....#....#....\n" +
                "#.#..#..#.#....#.#..#....#....#....#..#.\n" +
                "#..#..##..####.#..#.#....####.#.....##..\n"
        day10()
            .withDefaultInput()
            .hasPartOneResult(14160)
            .hasPartTwoResult(part2result)
    }

    test("December 11th") {
        day11()
            .withDefaultInput()
            .hasPartOneResult(88208)
            .hasPartTwoResult(21115867968)
    }

    test("December 12th") {
        day12()
            .withDefaultInput()
            .hasPartOneResult(462)
            .hasPartTwoResult(451)
    }

    test("December 13th") {
        day13()
            .withDefaultInput()
            .hasPartOneResult(5185)
            .hasPartTwoResult(23751)
    }

    test("December 14th") {
        day14()
            .withDefaultInput()
            .hasPartOneResult(979)
            .hasPartTwoResult(29044)
    }

    test("December 15th") {
        day15()
            .withExampleInput()
            .hasPartOneResult(26)
            .hasPartTwoResult(56000011)
            .withDefaultInput()
            .hasPartOneResult(5176944)
            .hasPartTwoResult(13350458933732)
    }

    test("December 16th") {
        day16()
            .withDefaultInput()
            .hasPartOneResult(1724)
            .hasPartTwoResult(2283)
    }

    test("December 17th") {
        day17()
            .withDefaultInput()
            .hasPartOneResult(3092)
            .hasPartTwoResult(1528323699442)
    }

    test("December 18th") {
        day18()
            .withDefaultInput()
            .hasPartOneResult(3448)
            .hasPartTwoResult(2052)
    }

    //Slow, ~3 minutes
    test("December 19th") {
        day19()
            .withDefaultInput()
            .hasPartOneResult(1550)
            .hasPartTwoResult(18630)
    }

    test("December 20th") {
        day20()
            .withDefaultInput()
            .hasPartOneResult(5962)
            .hasPartTwoResult(9862431387256)
    }

    test("December 21st") {
        day21()
            .withExampleInput()
            .hasPartOneResult(152)
            .hasPartTwoResult(301)
            .withDefaultInput()
            .hasPartOneResult(268597611536314)
            .hasPartTwoResult(3451534022348)
    }

    test("December 22nd") {
        day22()
            .withExampleInput()
            .hasPartOneResult(6032)
            .hasPartTwoResult(5031)
            .withDefaultInput()
            .hasPartOneResult(159034)
            .hasPartTwoResult(147245)
    }

    test("December 23rd") {
        day23()
            .withDefaultInput()
            .hasPartOneResult(4109)
            .hasPartTwoResult(1055)
    }

    test("December 24th") {
        day24()
            .withExampleInput()
            .hasPartOneResult(18)
            .hasPartTwoResult(54)
            .withDefaultInput()
            .hasPartOneResult(281)
            .hasPartTwoResult(807)
    }

    test("December 25th") {
        day25()
            .withDefaultInput()
            .hasPartOneResult("2=10---0===-1--01-20")
    }
})

