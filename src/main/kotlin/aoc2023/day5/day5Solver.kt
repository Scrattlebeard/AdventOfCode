package aoc2023.day5

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Pair<List<Long>, Map<MapTypes, List<Pair<LongRange, Long>>>>> {
    return setup {
        day(5)
        year(2023)

        //input("example.txt")

        parser {
            val groups = it.getStringsGroupedByEmptyLine()
            val seeds = groups[0][0].replace("seeds: ", "").asListOfLongs()
            val mapMap = mutableMapOf<MapTypes, List<Pair<LongRange, Long>>>()
            groups.drop(1).forEach {
                val currentMap = createMap(it.drop(1))
                when (it[0]) {
                    "seed-to-soil map:" -> {
                        mapMap[MapTypes.Seed] = currentMap
                    }

                    "soil-to-fertilizer map:" -> {
                        mapMap[MapTypes.Soil] = currentMap
                    }

                    "fertilizer-to-water map:" -> {
                        mapMap[MapTypes.Fertilizer] = currentMap
                    }

                    "water-to-light map:" -> {
                        mapMap[MapTypes.Water] = currentMap
                    }

                    "light-to-temperature map:" -> {
                        mapMap[MapTypes.Light] = currentMap
                    }

                    "temperature-to-humidity map:" -> {
                        mapMap[MapTypes.Temperature] = currentMap
                    }

                    "humidity-to-location map:" -> {
                        mapMap[MapTypes.Humidity] = currentMap
                    }
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
                while (mapOrder.containsKey(type)) {
                    val currentMap = maps[type]!!
                    val match =
                        currentMap.firstOrNull { it.first.first <= currentValue && it.first.last >= currentValue }
                    if (match != null) {
                        currentValue = match.second + (currentValue - match.first.first)
                    }
                    type = mapOrder[type]!!
                }
                closest = minOf(currentValue, closest)
            }
            closest.toString()
        }

        partTwo {
            val seedRanges = it.first.windowed(2, 2).map { it[0] until it[0] + it[1] }
            val maps = it.second
            var closest = Long.MAX_VALUE

            seedRanges.forEach {
                var type: MapTypes? = MapTypes.Seed
                var currentValues = listOf(it)
                do {
                    val currentMap = maps[type]!!
                    currentValues = currentValues.mapValuesTo2(currentMap)
                    type = mapOrder.getOrDefault(type, null)
                } while (type != MapTypes.Location)

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

fun createMap(lines: List<String>): List<Pair<LongRange, Long>> {
    val res = mutableListOf<Pair<LongRange, Long>>()
    lines.map { it.asListOfLongs() }
        .forEach { res.add(it[1] until it[1] + it[2] to it[0]) }
    return res.sortedBy { it.first.first }
}

fun List<LongRange>.mapValuesTo2(destination: List<Pair<LongRange, Long>>): List<LongRange> {
    val res = mutableListOf<LongRange>()

    val sourceIterator = this.listIterator()
    val destIterator = destination.iterator()
    var currentDest = destIterator.next()

    while (sourceIterator.hasNext()) {
        var sourceRange = sourceIterator.next()

        while(!sourceRange.isEmpty()) {
            while (currentDest.first.last < sourceRange.first && destIterator.hasNext()) {
                currentDest = destIterator.next()
            }

            val destRange = currentDest.first

            if(destRange.last < sourceRange.first && !destIterator.hasNext()) {
                res.add(sourceRange)
                while (sourceIterator.hasNext()) {
                    res.add(sourceIterator.next())
                }
                break
            }

            if (sourceRange.first < destRange.first) {
                val last = minOf(sourceRange.last, destRange.first)
                res.add(sourceRange.first..last)
                if (last == sourceRange.last) {
                    break
                }
            }

            val intersection = sourceRange.intersect(destRange)
            val offset = intersection.first - destRange.first
            res.add((currentDest.second + offset)until(intersection.size() + currentDest.second + offset))
            sourceRange = sourceRange.startAt(intersection.last + 1)
        }
    }

    return res.sortedBy { it.first }
}

fun List<LongRange>.mapValuesTo(destination: List<Pair<LongRange, Long>>): MutableList<LongRange> {
    val res = mutableListOf<LongRange>()

    this.forEach { range ->
        var remaining = range
        while (true) {
            val match = destination.firstOrNull { it.first.intersects(remaining) }
            if (match == null) {
                res.add(remaining)
                break
            }

            val matchedRange = match.first.intersect(remaining)
            val destStart = match.second + (remaining.first - match.first.first)
            res.add(destStart until (destStart + matchedRange.count()))

            if (matchedRange.first > remaining.first) {
                res.add(remaining.first until matchedRange.first)
            }

            if (matchedRange.last < remaining.last) {
                remaining = (matchedRange.last + 1)..remaining.last
            } else {
                break
            }
        }
    }

    return res
}