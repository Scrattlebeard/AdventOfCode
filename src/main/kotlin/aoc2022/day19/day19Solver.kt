package aoc2022.day19

import kotlinx.coroutines.*
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
            val quals =
                it.map { it.maximizeGeodes(State(24, emptyResourceMap, emptyResourceMap.plus(Resource.Ore to 1)), 0) * it.id }
            quals.sum()
                .toString()
        }

        partTwo {
            val startState = State(32, emptyResourceMap, emptyResourceMap.plus(Resource.Ore to 1))
            runBlocking {
                val first =
                    async { it[0].maximizeGeodes(startState, 0) }
                val second =
                    async { it[1].maximizeGeodes(startState, 0) }
                val third =
                    async { it[2].maximizeGeodes(startState, 0) }

                println("Solved first blueprint with result ${first.await()}")
                println("Solved second blueprint with result ${second.await()}")
                println("Solved third blueprint with result ${third.await()}")
                (first.await() * second.await() * third.await()).toString()
            }
        }
    }
}

val emptyResourceMap = Resource.values().associateWith { 0 }

data class State(val turnsRemaining: Int, val resources: Map<Resource, Int>, val robots: Map<Resource, Int>)

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
            Resource.Obsidian to geodeCost2.toInt()
        )
    }

    fun maximizeGeodes(state: State, currentBest: Int): Int {
        val (turnsRemaining, resourcesAvailable, robots) = state

        if (turnsRemaining == 0) {
            return resourcesAvailable[Resource.Geode]!!
        }

        //Always construct geode robots
        if (resourcesAvailable.canAfford(prices[Resource.Geode]!!)) {
            return maximizeGeodes(buildRobot(Resource.Geode, state), currentBest)
        }

        val options = mutableListOf<State>()

        //Building ore robots are only limited by the amount of ore we can spend per minute
        if (resourcesAvailable.canAfford(prices[Resource.Ore]!!) && robots[Resource.Ore]!! < limits[Resource.Ore]!!) {
            options.add(buildRobot(Resource.Ore, state))
        }

        //Obsidian takes precedence over clay
        if (robots[Resource.Obsidian]!! < limits[Resource.Obsidian]!!) {
            if (resourcesAvailable.canAfford(prices[Resource.Obsidian]!!)) {
                options.add(buildRobot(Resource.Obsidian, state))
            } else if (resourcesAvailable.canAfford(prices[Resource.Clay]!!) && robots[Resource.Clay]!! < limits[Resource.Clay]!!) {
                options.add(buildRobot(Resource.Clay, state))
            }
        }

        //Only wait for a resource if we are potentially bottlenecked and have at least one robot collecting said ressource
        if (resourcesAvailable.any { it.key != Resource.Geode && robots[it.key]!! > 0 && it.value < limits[it.key]!! }) {
            options.add(wait(state))
        }

        var newBest = currentBest
        options.forEach {
            if (getUpperBound(it) > newBest) {
                newBest = maxOf(newBest, maximizeGeodes(it, newBest))
            }
        }

        return newBest
    }

    fun getUpperBound(state: State): Int {
        val (turnsRemaining, resourcesAvailable, robots) = state
        val fromCurrentBots = robots[Resource.Geode]!! * turnsRemaining
        val oreBudget =
            resourcesAvailable[Resource.Ore]!! + ((robots[Resource.Ore]!! + turnsRemaining - 1) * turnsRemaining)
        val obsidianBudget =
            resourcesAvailable[Resource.Obsidian]!! + ((robots[Resource.Obsidian]!! + turnsRemaining - 1) * turnsRemaining)
        val numFutureBots = minOf(
            oreBudget / prices[Resource.Geode]!!.ore,
            obsidianBudget / prices[Resource.Geode]!!.obsidian,
            turnsRemaining
        )
        val fromFutureBots = (turnsRemaining - 1 downTo turnsRemaining - numFutureBots).sum()
        return fromCurrentBots + fromFutureBots + resourcesAvailable[Resource.Geode]!!
    }

    fun wait(state: State): State {
        val (turnsRemaining, resourcesAvailable, robots) = state
        return State(turnsRemaining - 1, resourcesAvailable.sumByKey(robots), robots)
    }

    fun buildRobot(toBuild: Resource, state: State): State {
        val (turnsRemaining, resourcesAvailable, robots) = state
        return State(
            turnsRemaining - 1,
            resourcesAvailable.pay(prices[toBuild]!!).sumByKey(robots),
            robots.plus(toBuild to robots[toBuild]!! + 1)
        )
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