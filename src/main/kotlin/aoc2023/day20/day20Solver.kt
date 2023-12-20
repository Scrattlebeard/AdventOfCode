package aoc2023.day20

import lib.*

suspend fun main() {
    setupChallenge().solveChallenge()
}

fun setupChallenge(): Challenge<List<Module>> {
    return setup {
        day(20)
        year(2023)

        //input("example.txt")

        parser {
            it.readLines()
                .map {
                    val parts = it.split(" -> ")
                    Module(parts[0][0], parts[0].drop(1), parts[1].split(", "))
                }
        }

        partOne {
            modules = it.associateBy { it.name }
            var outputLows = 0L
            var outputHighs = 0L

            repeat(1000) {
                val toSend = mutableListOf(Signal("button", "roadcaster", SignalType.Low))
                while (toSend.any()) {
                    val signal = toSend.first()
                    toSend.remove(signal)
                    if (modules.containsKey(signal.to)) {
                        toSend.addAll(modules[signal.to]!!.handle(signal.from, signal.type))
                    } else {
                        if(signal.type == SignalType.High) outputHighs++ else outputLows++
                    }
                }
            }
            ((modules.values.sumOf { it.lowSignals } + outputLows) * (modules.values.sumOf { it.highSignals } + outputHighs)).toString()
        }

        partTwo {
            modules = it.associateBy { it.name }
            modules.forEach { it.value.initConnections() }

            //Lots of assumptions here - that our output "rx" is dependent on exactly one conjunction outputting "low"...
            val toMonitor = modules.values.first { it.destinations.contains("rx") }.inputs!!.keys
            val iterationTracker = toMonitor.associateWith { 0 }.toMutableMap()

            var i = 0
            while (iterationTracker.any { it.value < 1 }) {
                i++
                val toSend = mutableListOf(Signal("button", "roadcaster", SignalType.Low))
                while (toSend.any()) {
                    val signal = toSend.first()
                    toSend.remove(signal)
                    if (modules.containsKey(signal.to)) {
                        if(signal.from in toMonitor && signal.type == SignalType.High) {
                            val prev = iterationTracker[signal.from]!!
                            iterationTracker[signal.from] = i - iterationTracker[signal.from]!!
                            if(prev != 0 && iterationTracker[signal.from] != prev) {
                                throw Exception("Irregularly output frequency")
                            }
                       }
                      toSend.addAll(modules[signal.to]!!.handle(signal.from, signal.type))
                    }
                }
            }

            iterationTracker.values.fold(1L) {s, n -> s * n}.toString()
        }
    }
}

var modules = mapOf<String, Module>()

enum class SignalType { High, Low }
data class Signal(val from: String, val to: String, val type: SignalType)
data class Module(val ope: Char, val name: String, val destinations: List<String>) {
    var highSignals = 0L
    var lowSignals = 0L
    var state = false
    var inputs: MutableMap<String, SignalType>? = null


    fun handle(origin: String, signal: SignalType): List<Signal> {
        if (signal == SignalType.High) highSignals++ else lowSignals++
        val toSend = when (ope) {
            '%' -> flipFlop(signal)
            '&' -> conjunction(origin, signal)
            'b' -> destinations.map { Signal(name, it, signal) }
            else -> throw Exception("Unknown operation $ope")
        }
        return toSend
    }

    fun initConnections() {
        if(ope == '&') {
            inputs = modules.filter { it.value.destinations.contains(name) }.keys.associateWith { SignalType.Low }
                .toMutableMap()
        }
    }

    private fun flipFlop(signal: SignalType): List<Signal> {
        if (signal == SignalType.High) return listOf()

        state = !state
        return destinations.map { Signal(name, it, if (state) SignalType.High else SignalType.Low) }
    }

    private fun conjunction(origin: String, signal: SignalType): List<Signal> {
        if(inputs == null) {
            initConnections()
        }

        inputs!![origin] = signal
        return destinations.map {
            Signal(
                name,
                it,
                if (inputs!!.values.all { it == SignalType.High }) SignalType.Low else SignalType.High
            )
        }
    }
}