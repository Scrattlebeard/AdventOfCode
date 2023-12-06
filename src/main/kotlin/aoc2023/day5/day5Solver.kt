package aoc2023.day5

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<List<Long>, Map<MapTypes, Map<LongProgression, Long>>>> {
    return setup {
        day(5)
        year(2023)

        //input("example.txt")

        parser {
            val groups = it.getStringsGroupedByEmptyLine()
            val seeds = groups[0][0].replace("seeds: ", "").asListOfLongs()
            val mapMap = mutableMapOf<MapTypes, Map<LongProgression, Long>>()
            groups.drop(1).forEach {
                val currentMap = createMap(it.drop(1))
                when (it[0]) {
                    "seed-to-soil map:" -> { mapMap[MapTypes.Seed] = currentMap }
                    "soil-to-fertilizer map:" -> { mapMap[MapTypes.Soil] = currentMap }
                    "fertilizer-to-water map:" -> { mapMap[MapTypes.Fertilizer] = currentMap }
                    "water-to-light map:" -> { mapMap[MapTypes.Water] = currentMap }
                    "light-to-temperature map:" -> { mapMap[MapTypes.Light] = currentMap }
                    "temperature-to-humidity map:" -> { mapMap[MapTypes.Temperature] = currentMap }
                    "humidity-to-location map:" -> { mapMap[MapTypes.Humidity] = currentMap }
                }
            }

            seeds to mapMap
        }

        partOne {
            val seeds = it.first
            val maps = it.second
            var closest = Long.MAX_VALUE
            seeds.forEach {
                var type = MapTypes.Seed
                var currentValue = it
                while(mapOrder.containsKey(type)) {
                    val currentMap = maps[type]!!
                    val match  = currentMap.keys.firstOrNull { it.first <= currentValue && it.last >= currentValue}
                    if(match != null) {
                        currentValue = currentMap[match]!! + (currentValue - match.first)
                    }
                    type = mapOrder[type]!!
                }
                closest = minOf(currentValue, closest)
            }
            closest.toString()
        }

        partTwo {
            val seedRanges = it.first.windowed(2, 2).map { (it[0] until it[0] + it[1]) as LongProgression }
            val maps = it.second
            var closest = Long.MAX_VALUE

            seedRanges.forEach {
                var type = MapTypes.Seed
                var currentValues = mutableListOf(it)
                while(mapOrder.containsKey(type)) {
                    val currentMap = maps[type]!!
                    currentValues = currentValues.mapValuesTo(currentMap)
                    type = mapOrder[type]!!
                }
                closest = minOf(currentValues.minOf { it.first }, closest)
            }

            closest.toString()
        }
    }
}

enum class MapTypes {
    Seed,
    Soil,
    Fertilizer,
    Water,
    Light,
    Temperature,
    Humidity,
    Location
}

val mapOrder = mapOf(
    Pair(MapTypes.Seed, MapTypes.Soil),
    Pair(MapTypes.Soil, MapTypes.Fertilizer),
    Pair(MapTypes.Fertilizer, MapTypes.Water),
    Pair(MapTypes.Water, MapTypes.Light),
    Pair(MapTypes.Light, MapTypes.Temperature),
    Pair(MapTypes.Temperature, MapTypes.Humidity),
    Pair(MapTypes.Humidity, MapTypes.Location)
)

fun createMap(lines: List<String>): Map<LongProgression, Long> {
    val res = mutableMapOf<LongProgression, Long>()
    lines.map { it.asListOfLongs() }
        .forEach { res[it[1] until it[1] + it[2]] = it[0] }
    return res
}

fun List<LongProgression>.mapValuesTo(destination: Map<LongProgression, Long>): MutableList<LongProgression> {
    val res = mutableListOf<LongProgression>()

    this.forEach { range ->
        var remaining = range
        while (true) {
            val match = destination.keys.sortedBy { it.first }.firstOrNull { it.intersects(remaining)}
            if(match == null) {
                res.add(remaining)
                break
            }

            val matchedRange = match.intersect(remaining)
            val destStart = destination[match]!! + (remaining.first - match.first)
            res.add(destStart until (destStart + matchedRange.count()))

            if(matchedRange.first > remaining.first) {
                res.add(remaining.first until matchedRange.first)
            }

            if(matchedRange.last < remaining.last) {
                remaining = (matchedRange.last + 1) .. remaining.last
            }
            else {
                break
            }
        }
    }

    return res
}