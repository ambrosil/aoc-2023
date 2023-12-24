import utils.leastCommonMultiple

data class Pulse(val high: Boolean, val source: String, val destination: String)

private abstract class Module(val name: String, val destinations: List<String>) {
    abstract fun receive(pulse: Pulse): List<Pulse>
    fun send(high: Boolean): List<Pulse> =
        destinations.map { Pulse(high, name, it) }
}

private class Broadcaster(destinations: List<String>) : Module("broadcaster", destinations) {
    override fun receive(pulse: Pulse): List<Pulse> = send(pulse.high)
}

private class FlipFlop(name: String, destinations: List<String>) : Module(name, destinations) {
    private var on = false

    override fun receive(pulse: Pulse): List<Pulse> =
        if (pulse.high) emptyList()
        else {
            on = !on
            send(on)
        }
}

private class Conjunction(name: String, destinations: List<String>) : Module(name, destinations) {
    private val memory = mutableMapOf<String, Boolean>()

    fun add(source: String) {
        if (source !in memory) memory[source] = false
    }

    override fun receive(pulse: Pulse): List<Pulse> {
        memory[pulse.source] = pulse.high
        return send(memory.values.any { !it })
    }
}

interface Debugger {
    fun pulse(pulse: Pulse)
}

private class Part1Debugger : Debugger {
    var high = 0
    var low = 0
    override fun pulse(pulse: Pulse) {
        if (pulse.high) high++ else low++
    }
}

private class Part2Debugger(val sources: Set<String>) : Debugger {
    val seen = mutableSetOf<String>()

    override fun pulse(pulse: Pulse) {
        if (pulse.high && pulse.source in sources)
            seen += pulse.source
    }
}

fun main() {

    lateinit var modules: Map<String, Module>

    fun parseModules(input: List<String>) {
        modules = input.associate { row ->
            val type = row.first()
            val name = row.substring(1).substringBefore(" ")
            val destinations = row.substringAfter(">").split(",").map { it.trim() }.filter { it.isNotEmpty() }

            when (type) {
                'b' -> "broadcaster" to Broadcaster(destinations)
                '&' -> name to Conjunction(name, destinations)
                '%' -> name to FlipFlop(name, destinations)
                else -> throw IllegalArgumentException("No such module: $type from $row")
            }
        }

        val conjunctions = modules.values.filterIsInstance<Conjunction>().associateBy { it.name }
        modules.values.forEach { module ->
            module.destinations.forEach { destination ->
                conjunctions[destination]?.add(module.name)
            }
        }
    }

    fun button(debugger: Debugger? = null) {
        val messages = ArrayDeque<Pulse>().apply {
            add(Pulse(false, "button", "broadcaster"))
        }
        while (messages.isNotEmpty()) {
            with(messages.removeFirst()) {
                debugger?.pulse(this)
                modules[destination]?.receive(this)?.forEach { messages.add(it) }
            }
        }
    }

    fun part1(input: List<String>): Int {
        parseModules(input)
        val debugger = Part1Debugger()
        repeat(1000) { button(debugger) }
        return debugger.high * debugger.low
    }

    fun part2(input: List<String>): Long {
        parseModules(input)
        val sourceForRx = modules.values.first { "rx" in it.destinations }
        val lookFor = modules.values
            .filter { sourceForRx.name in it.destinations }.toMutableSet()
            .associate { it.name to 0L }
            .toMutableMap()
        val debugger = Part2Debugger(lookFor.keys)
        var count = 0

        while (lookFor.values.any { it == 0L }) {
            count++
            button(debugger)
            debugger.seen.forEach { name ->
                if (lookFor.getValue(name) == 0L) {
                    lookFor[name] = count.toLong()
                }
            }
            debugger.seen.clear()
        }

        return lookFor.values.reduce { acc, i -> leastCommonMultiple(acc, i) }
    }

    val input = readInput("inputs/Day20")
    part1(input).println()
    part2(input).println()
}

