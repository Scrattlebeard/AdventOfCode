package aoc2021.day16

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<Packet> {
    return setup {
        day(16)
        year(2021)

        input("example6.txt")
        input("input.txt")

        parser {
            buildPacket(it.readText().trim()
                .map { it.digitToInt(16).toString(2).padStart(4, '0') }
                .fold("") { s, b -> s + b })
        }

        partOne {
            it.getVersionSum().toString()
        }

        partTwo {
            it.getValue().toString()
        }
    }
}

fun buildPacket(bits: String): Packet {
    val type = bits.slice(3..5).toInt(2)
    return when (type) {
        4 -> LiteralPacket(bits)
        else -> {
            val lengthType = bits[6]
            if (lengthType == '0') Type0OperatorPacket(bits) else Type1OperatorPacket(bits)
        }
    }
}

abstract class Packet(bits: String) {
    val version = bits.slice(0..2).toInt(2)
    val type = bits.slice(3..5).toInt(2)
    abstract val length: Int

    abstract fun getVersionSum(): Long
    abstract fun getValue(): Long
}

class LiteralPacket(bits: String) : Packet(bits) {

    override val length: Int
    private val value: Long
    private var numSequences: Int = 0

    init {
        val sb = StringBuilder()
        var done = false
        var i = 6
        while (!done) {
            done = bits[i] == '0'
            val newI = i + 5
            sb.append(bits.subSequence(i + 1, newI))
            numSequences++
            i = newI
        }
        length = 6 + sb.length + numSequences
        value = sb.toString().toLong(2)
    }

    override fun getVersionSum() = version.toLong()
    override fun getValue() = value
}

abstract class OperatorPacket(bits: String) : Packet(bits) {
    val subPackets = mutableListOf<Packet>()

    override fun getVersionSum() = version + subPackets.sumOf { it.getVersionSum() }

    override fun getValue(): Long {
        return when (type) {
            0 -> subPackets.sumOf { it.getValue() }
            1 -> subPackets.fold(1L) { prod, packet -> prod * packet.getValue() }
            2 -> subPackets.minOf { it.getValue() }
            3 -> subPackets.maxOf { it.getValue() }
            5 -> if (subPackets[0].getValue() > subPackets[1].getValue()) 1 else 0
            6 -> if (subPackets[0].getValue() < subPackets[1].getValue()) 1 else 0
            7 -> if (subPackets[0].getValue() == subPackets[1].getValue()) 1 else 0
            else -> throw Exception("Unknown operator packet type: $type")
        }
    }
}

class Type0OperatorPacket(bits: String) : OperatorPacket(bits) {

    private val subLength = bits.slice(7..21).toInt(2)
    override val length = 22 + subLength

    init {
        var i = 22
        while (i < subLength + 22) {
            val packet = buildPacket(bits.slice(i until length))
            subPackets.add(packet)
            i += packet.length
        }
    }
}

class Type1OperatorPacket(bits: String) : OperatorPacket(bits) {

    private val numSubPackets = bits.slice(7..17).toInt(2)
    override val length: Int

    init {
        var i = 18
        var n = 0
        while (n < numSubPackets) {
            val packet = buildPacket(bits.slice(i until bits.length))
            n++
            i += packet.length
            subPackets.add(packet)
        }
        length = 18 + subPackets.sumOf { it.length }
    }
}