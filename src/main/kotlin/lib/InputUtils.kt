package lib

import java.io.File

val newline: String = System.lineSeparator()

fun File.getStringsGroupedByEmptyLine(): List<List<String>> {
    return this.readText()
        .trimEnd()
        .split("$newline$newline")
        .map { it.split(newline) }
}

fun List<String>.get2DArrayOfColumns(): Array<Array<Char>> {
    val height = this.size
    val width = this.first().length

    return Array(width) { i -> Array(height) { j -> this[j][i] } }
}

fun List<String>.get2DArrayOfColumns(height: Int, width: Int, padding: Char): Array<Array<Char>> {
    return Array(width) { i -> Array(height) { j -> if(j < this.size && i < this[j].length) this[j][i] else padding } }
}

fun List<String>.readStringsAsDigits(): List<List<Int>> {
    return this.map {
        it.map {
            "$it".toInt()
        }
    }
}

fun String.asListofInts(separator: String = " "): List<Int> {
    return this.split(separator).filter { it.isNotEmpty() }.map { it.trim().toInt() }
}

fun String.asListofLongs(separator: String = " "): List<Long> {
    return this.split(separator).filter { it.isNotEmpty() }.map { it.trim().toLong() }
}