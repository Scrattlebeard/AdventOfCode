package aoc2022.day19

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Factory>> {
    return setup {
        day(19)
        year(2022)

        //input("example.txt")
        input("input.txt")

        parser {
            it.readLines()
                .map { Factory(it) }
        }

        partOne {
            val quals = it.map { it.maximizeGeodes(24, emptyResourceMap, emptyResourceMap.plus(Resource.Ore to 1)) * it.id }
            quals.sum()
                .toString()
        }

        partTwo {
            val first = it[0].maximizeGeodes(32, emptyResourceMap, emptyResourceMap.plus(Resource.Ore to 1))
            println("Solved first blueprint with result $first")
            val second = it[1].maximizeGeodes(32, emptyResourceMap, emptyResourceMap.plus(Resource.Ore to 1))
            println("Solved second blueprint with result $second")
            val third = it[2].maximizeGeodes(32, emptyResourceMap, emptyResourceMap.plus(Resource.Ore to 1))
            println("Solved third blueprint with result $third")
            (first * second * third).toString()
        }
    }
}

val emptyResourceMap = Resource.values().associateWith { 0 }

class Factory(blueprint: String) {

    val id: Int
    val prices: Map<Resource, Price>
    val limits: Map<Resource, Int>

    init {
        val (idVal, oreCost, clayCost, obsidianCost1, obsidianCost2, geodeCost1, geodeCost2) = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian."""
            .toRegex()
            .find(blueprint)!!
            .destructured

        id = idVal.toInt()
        prices = mapOf(
            Resource.Ore to Price(oreCost.toInt(), 0, 0),
            Resource.Clay to Price(clayCost.toInt(), 0, 0),
            Resource.Obsidian to Price(obsidianCost1.toInt(), obsidianCost2.toInt(), 0),
            Resource.Geode to Price(geodeCost1.toInt(), 0, geodeCost2.toInt())
        )

        limits = mapOf(
            Resource.Ore to prices.values.maxOf { it.ore },
            Resource.Clay to obsidianCost2.toInt(),
            Resource.Obsidian to geodeCost2.toInt(),
            Resource.Geode to Int.MAX_VALUE
        )
    }

    fun maximizeGeodes(turnsRemaining: Int, resourcesAvailable: Map<Resource, Int>, robots: Map<Resource, Int>): Int {
        if (turnsRemaining == 0)
            return resourcesAvailable[Resource.Geode]!!

        //Always construct geode robots
        if (resourcesAvailable.canAfford(prices[Resource.Geode]!!)) {
            val upd = buildRobot(Resource.Geode, resourcesAvailable, robots)
            return maximizeGeodes(turnsRemaining - 1, upd.first, upd.second)
        }

        val options = mutableListOf<Pair<Map<Resource, Int>, Map<Resource, Int>>>()

        //Building ore robots are only limited by the amount of ore we can spend per minute
        if (resourcesAvailable.canAfford(prices[Resource.Ore]!!) && robots[Resource.Ore]!! < limits[Resource.Ore]!!) {
            options.add(buildRobot(Resource.Ore, resourcesAvailable, robots))
        }

        val canBuyObsidian = robots[Resource.Obsidian]!! < limits[Resource.Obsidian]!!

        //Obsidian takes precedence over clay
        if (resourcesAvailable.canAfford(prices[Resource.Obsidian]!!) && canBuyObsidian) {
            options.add(buildRobot(Resource.Obsidian, resourcesAvailable, robots))
        } else if (resourcesAvailable.canAfford(prices[Resource.Clay]!!) && robots[Resource.Clay]!! < limits[Resource.Clay]!! && canBuyObsidian) {
            options.add(buildRobot(Resource.Clay, resourcesAvailable, robots))
        }

        //Only wait for a resource if we are potentially bottlenecked and have at least one robot collecting said ressource
        if (resourcesAvailable.any { it.key != Resource.Geode && robots[it.key]!! > 0 && it.value < limits[it.key]!! }) {
            options.add(wait(resourcesAvailable, robots))
        }

        var currentBest = maximizeGeodes(turnsRemaining - 1, options.first().first, options.first().second)
        options.drop(1).forEach {
            if(getUpperBound(it, turnsRemaining - 1) > currentBest)
                currentBest = maxOf(currentBest, maximizeGeodes(turnsRemaining - 1, it.first, it.second))
        }

        return currentBest
    }

    fun getUpperBound(option: Pair<Map<Resource, Int>, Map<Resource, Int>>, remainingTurns: Int) : Int {
        val fromCurrentBots = option.second[Resource.Geode]!! * remainingTurns
        val oreBudget = option.first[Resource.Ore]!! + ((option.second[Resource.Ore]!! + remainingTurns - 1) * remainingTurns)
        val obsidianBudget = option.first[Resource.Obsidian]!! + ((option.second[Resource.Obsidian]!! + remainingTurns - 1) * remainingTurns)
        val numFutureBots = minOf(oreBudget / prices[Resource.Geode]!!.ore, obsidianBudget / prices[Resource.Geode]!!.obsidian, remainingTurns)
        val fromFutureBots = (remainingTurns - 1 downTo remainingTurns - numFutureBots).sum()
        return fromCurrentBots + fromFutureBots + option.first[Resource.Geode]!!
    }

    fun wait(
        resourcesAvailable: Map<Resource, Int>,
        robots: Map<Resource, Int>
    ): Pair<Map<Resource, Int>, Map<Resource, Int>> {
        return resourcesAvailable.sumByKey(robots) to robots
    }

    fun buildRobot(
        toBuild: Resource,
        resourcesAvailable: Map<Resource, Int>,
        robots: Map<Resource, Int>
    ): Pair<Map<Resource, Int>, Map<Resource, Int>> {
        return resourcesAvailable.pay(prices[toBuild]!!)
            .sumByKey(robots) to robots.plus(toBuild to robots[toBuild]!! + 1)
    }
}

fun Map<Resource, Int>.canAfford(price: Price): Boolean {
    return (price.ore <= this.getOrDefault(Resource.Ore, 0))
            && price.clay <= this.getOrDefault(Resource.Clay, 0)
            && price.obsidian <= this.getOrDefault(Resource.Obsidian, 0)
}

fun Map<Resource, Int>.pay(price: Price): Map<Resource, Int> {
    return mapOf(
        Resource.Ore to this[Resource.Ore]!! - price.ore,
        Resource.Clay to this[Resource.Clay]!! - price.clay,
        Resource.Obsidian to this[Resource.Obsidian]!! - price.obsidian,
        Resource.Geode to this[Resource.Geode]!!
    )
}

fun Map<Resource, Int>.sumByKey(other: Map<Resource, Int>): Map<Resource, Int> {
    return this.mapValues { it.value + other.getOrDefault(it.key, 0) }
}

data class Price(val ore: Int, val clay: Int, val obsidian: Int)

enum class Resource {
    Ore,
    Clay,
    Obsidian,
    Geode
}