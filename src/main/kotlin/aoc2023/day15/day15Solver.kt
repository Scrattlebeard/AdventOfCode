package aoc2023.day15

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<String>> {
    return setup {
        day(15)
        year(2023)

        //input("example.txt")

        parser {
            it.readText().trim().split(",")
        }

        partOne {
            val verifications = it.map { it.calculateHASH() }
            verifications.sum().toString()
        }

        partTwo {
            val boxes = (0..255).associateWith { Box(listOf()) }
            it.forEach { inst -> if (inst.contains("-")) boxes.remove(inst) else boxes.set(inst) }
            val powers = boxes.entries.map { it.value.getFocusPower(it.key) }
            powers.sum().toString()

        }
    }
}

fun String.calculateHASH(): Int {
    return this.fold(0) { sum, c -> ((sum + c.code) * 17) % 256 }
}

data class Lens(val label: String, var strength: Int)
data class Box(var contents: List<Lens>)

fun Map<Int, Box>.remove(inst: String) {
    val label = inst.dropLast(1)
    val box = this[label.calculateHASH()]!!
    val lens = box.contents.firstOrNull() { it.label == label }
    if (lens != null) {
        box.contents = box.contents.minus(lens)
    }
}

fun Map<Int, Box>.set(inst: String) {
    val instParts = inst.split("=")
    val label = instParts.first()
    val box = this[label.calculateHASH()]!!
    if (box.contents.any { it.label == label }) {
        val lens = box.contents.first { it.label == label }
        lens.strength = instParts.last().toInt()
    } else {
        box.contents = box.contents.plus(Lens(label, instParts.last().toInt()))
    }

}

fun Box.getFocusPower(boxnumber: Int): Long {
    return contents.foldIndexed(0) { i, s, l ->
            s + (boxnumber + 1) * (i + 1) * l.strength
        }
}