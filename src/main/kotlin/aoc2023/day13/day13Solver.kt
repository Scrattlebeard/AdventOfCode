package aoc2023.day13

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Area>> {
    return setup {
        day(13)
        year(2023)

        //input("example.txt")

        parser {
            it.getStringsGroupedByEmptyLine()
                .map {
                    Area(
                        it.toMutableList(),
                        it.first().indices.map { i -> it.map { it[i] }.fold("") { s, c -> s + c } }.toMutableList()
                    )
                }
        }

        partOne {
            it.sumOf { it.findReflectionValue() }
                .toString()
        }

        partTwo {
            val mirrors = it.map { fixSmudges(it) to it.findReflectionValue() }
            var sum = 0L
            mirrors.forEach {
                val iterator = it.first.iterator()
                while(iterator.hasNext()) {
                    val area = iterator.next()
                    val res = area.findReflectionValue(it.second)
                    if(res > 0) {
                        sum += res
                        break
                    }
                }
            }
            sum.toString()
        }
    }
}

data class Area(val rows: MutableList<String>, val columns: MutableList<String>)

fun Area.findReflectionValue(ignore: Long = 0): Long {

    (0 until columns.size - 1).forEach { i ->
        val start = 0..i
        val end = i + 1..minOf(start.last + start.size(), columns.size - 1)
        if (rows.all { it.substring(start).reversed().take(end.size()) == it.substring(end) }) {
            val res = (i + 1).toLong()
            if(res != ignore) {
                return res
            }
        }
    }

    (0 until rows.size - 1).forEach { i ->
        val start = 0..i
        val end = i + 1..minOf(start.last + start.size(), rows.size - 1)
        if (columns.all { it.substring(start).reversed().take(end.size()) == it.substring(end) }) {
            val res = (i + 1) * 100L
            if(res != ignore) {
                return res
            }
        }
    }

    return 0
}

suspend fun fixSmudges(area: Area): Sequence<Area> = sequence {
    val rows = area.rows
    val columns = area.columns
    rows.indices.forEach { i ->
        columns.indices.forEach { j ->
            val newChar = rows[i][j].invert()
            val updatedRow = rows[i].take(j) + newChar + rows[i].takeLast(columns.size - (j + 1))
            val updatedColumn = columns[j].take(i) + newChar + columns[j].takeLast(rows.size - (i + 1))
            val newRows = rows.take(i) + updatedRow + rows.takeLast(rows.size - (i + 1))
            val newColumns = columns.take(j) + updatedColumn + columns.takeLast(columns.size - (j + 1))
            val res = Area(newRows.toMutableList(), newColumns.toMutableList())
            yield(res)
        }
    }
}

fun Char.invert(): Char {
    return when (this) {
        '.' -> '#'
        '#' -> '.'
        else -> this
    }
}