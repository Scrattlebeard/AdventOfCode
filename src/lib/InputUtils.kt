package lib

import java.io.File

val newline: String = System.lineSeparator()

fun File.getStringsGroupedByEmptyLine(): List<List<String>> {
    return this.readText()
        .trim()
        .split("$newline$newline")
        .map { it.split(newline) }
        .map { it.map { it.trim() } }
}

fun get2DArrayFromStringList(data: List<String>): Array<Array<Char>> {
    val height = data.size
    val width = data.first().length

    return Array(width) { i -> Array(height) { j -> data[j][i] } }
}

fun List<String>.readStringsAsDigits(): List<List<Int>> {
    return this.map {
        it.map {
            "$it".toInt()
        }
    }
}