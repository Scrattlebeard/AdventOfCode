package aoc2021.day19

import lib.*
import kotlin.math.abs

suspend fun main() {
    challenge<List<ScannerInfo>> {

        day(19)
        year(2021)

        //input("example.txt")

        parser {
            it.getStringsGroupedByEmptyLine()
                .map { it.subList(1, it.size) }
                .map { it.map { it.split(",").map { it.toInt() } } }
                .mapIndexed { i, points -> ScannerInfo(i, points.map { Triple(it[0], it[1], it[2]) }) }
        }

        solver {
            println("Solving challenge 1...")

            val startScanner = it.first { scanner ->
                it.minus(scanner).tryFindMatchingScanner(scanner.pairwiseDistances) != null
            }

            var truth = startScanner.points.toSet()

            var done = setOf(startScanner)
            while (done.size < it.count()) {
                val distList = truth.toList().getPairwiseDistances()
                val nextScanner = it.minus(done).tryFindMatchingScanner(distList)
                val convScanner = nextScanner!!.convertToCoordinates(distList)
                done = done.plus(nextScanner)
                truth = truth.plus(convScanner!!.points)
            }

            truth.count().toString()
        }

        solver {
            println("Solving challenge 2...")

            val startScanner = it.first { scanner ->
                it.minus(scanner).tryFindMatchingScanner(scanner.pairwiseDistances) != null
            }

            var truth = startScanner.points.toSet()

            var done = setOf(startScanner)
            var conv = setOf(startScanner)
            while (done.size < it.count()) {
                val distList = truth.toList().getPairwiseDistances()
                val nextScanner = it.minus(done).tryFindMatchingScanner(distList)
                val convScanner = nextScanner!!.convertToCoordinates(distList)
                done = done.plus(nextScanner)
                conv = conv.plus(convScanner!!)
                truth = truth.plus(convScanner.points)
            }

            conv.map { scanner -> conv.map { it.pos - scanner.pos }
                .maxOf { abs(it.first) + abs(it.second) + abs(it.third) } }
                .maxOf { it }
                .toString()
        }

    }
}

fun List<Triple<Int, Int, Int>>.getPairwiseDistances(): Map<Double, Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>>> {
    return this.dropLast(1).flatMapIndexed { i, first ->
        this.subList(i + 1, this.size).map { second ->
            first.distanceTo(second) to (first to second)
        }
    }.toMap()
}

fun List<ScannerInfo>.tryFindMatchingScanner(dists: Map<Double, Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>>>): ScannerInfo? {
    val matches = this.map { scanner -> scanner to scanner.pairwiseDistances.keys.count { it in dists.keys } }
    return matches.filter { it.second >= 66 } //66 is the expected number of matching distances for 12 overlapping points: 11 + 10 + 9 + ...
        .first { it.first.convertToCoordinates(dists) != null }
        .first
}

fun ScannerInfo.convertToCoordinates(truth: Map<Double, Pair<Triple<Int, Int, Int>, Triple<Int, Int, Int>>>): ScannerInfo? {
    val matchingPoints = this.pairwiseDistances.entries
        .filter { it.key in truth.keys }
        .associateWith { truth[it.key]!! }
        .map { it.key.value to it.value }
        .groupBy { it.first.first }
        .map { it.key to it.value.flatMap { listOf(it.second.first, it.second.second) } }
    val orientation = tryFindMatchingOrientation(matchingPoints) ?: return null
    val (rotation, delta) = orientation
    val si = ScannerInfo(this.number, this.points.map { rotation(it) + delta })
    si.pos = delta
    return si
}

fun tryFindMatchingOrientation(
    groups: List<Pair<Triple<Int, Int, Int>, List<Triple<Int, Int, Int>>>>
): Pair<(Triple<Int, Int, Int>) -> Triple<Int, Int, Int>, Triple<Int, Int, Int>>? {

    val pointMatches = groups.map {
        it.first to it.second.map { ele -> ele to it.second.count { it == ele } }.maxByOrNull { it.second }!!
    }.filter { it.second.second > 1 }

    return try {
        getRotations()
            .map { it to pointMatches.first().second.first - it(pointMatches.first().first) }
            .first { (rot, delta) ->
                pointMatches.count { rot(it.first) + delta == it.second.first } > 9
            }
    } catch (e: Exception) {
        null
    }
}

operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>): Triple<Int, Int, Int> =
    Triple(this.first + other.first, this.second + other.second, this.third + other.third)

operator fun Triple<Int, Int, Int>.minus(other: Triple<Int, Int, Int>): Triple<Int, Int, Int> =
    Triple(this.first - other.first, this.second - other.second, this.third - other.third)

fun getRotations(): List<(Triple<Int, Int, Int>) -> Triple<Int, Int, Int>> {
    return listOf(
        //Facing x
        { t -> Triple(t.first, t.second, t.third) },
        { t -> Triple(t.first, -t.second, -t.third) },
        { t -> Triple(t.first, t.third, -t.second) },
        { t -> Triple(t.first, -t.third, t.second) },
        //Facing -x
        { t -> Triple(-t.first, -t.second, t.third) },
        { t -> Triple(-t.first, t.second, -t.third) },
        { t -> Triple(-t.first, -t.third, -t.second) },
        { t -> Triple(-t.first, t.third, t.second) },
        //Facing y
        { t -> Triple(t.second, -t.first, t.third) },
        { t -> Triple(t.second, t.first, -t.third) },
        { t -> Triple(t.second, t.third, t.first) },
        { t -> Triple(t.second, -t.third, -t.first) },
        //Facing -y
        { t -> Triple(-t.second, t.first, t.third) },
        { t -> Triple(-t.second, t.third, -t.first) },
        { t -> Triple(-t.second, -t.third, t.first) },
        { t -> Triple(-t.second, -t.first, -t.third) },
        //Facing z
        { t -> Triple(t.third, t.second, -t.first) },
        { t -> Triple(t.third, t.first, t.second) },
        { t -> Triple(t.third, -t.second, t.first) },
        { t -> Triple(t.third, -t.first, -t.second) },
        //Facing -z
        { t -> Triple(-t.third, t.second, t.first) },
        { t -> Triple(-t.third, t.first, -t.second) },
        { t -> Triple(-t.third, -t.second, -t.first) },
        { t -> Triple(-t.third, -t.first, t.second) }
    )
}

fun Triple<Int, Int, Int>.getAllOrientations(): List<Triple<Int, Int, Int>> {
    return getRotations().map { it(this) }
}

fun List<Triple<Int, Int, Int>>.getAllOrientations(): List<List<Triple<Int, Int, Int>>> {
    return this.map {
        it.getAllOrientations()
    }
}

class ScannerInfo(val number: Int, val points: List<Triple<Int, Int, Int>>) {
    val pairwiseDistances = points.getPairwiseDistances()
    var pos = Triple(0, 0, 0)
}