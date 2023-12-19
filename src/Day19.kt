data class Rule(val varName: String = "", val value: Int = -1, val operator: Char = 'T', val ifTrue: String) {
    fun evaluate(map: Map<String, Int>) =
        when (operator) {
            '>' -> map.getValue(varName) > value
            '<' -> map.getValue(varName) < value
            else -> true
        }
}

fun main() {

    fun parseRule(s: String): Rule {
        val main = s.split(":")
        val cond = main.first()

        if (main.size == 1) {
            return Rule(ifTrue = cond)
        }

        val ifTrue = main.last()
        val varName: String
        val value: Int
        val operator: Char

        if (cond.contains(">")) {
            varName = cond.substringBefore(">")
            value = cond.substringAfter(">").toInt()
            operator = '>'
        } else {
            varName = cond.substringBefore("<")
            value = cond.substringAfter("<").toInt()
            operator = '<'
        }

        return Rule(varName, value, operator, ifTrue)
    }

    fun parseInput(input: List<String>): Pair<Map<String, List<Rule>>, List<Map<String, Int>>> {
        val dividerIdx = input.indexOfFirst { it.isBlank() }

        val wfs = input.take(dividerIdx).associate {
            val wfName = it.substringBefore("{")
            wfName to it.substringAfter("{").substringBefore("}")
                .split(",")
                .map { s -> parseRule(s) }
        }

        val states = input.drop(dividerIdx + 1).map {
            it.substringAfter("{").substringBefore("}").split(",").associate {
                it.substringBefore("=") to it.substringAfter("=").toInt()
            }
        }

        return wfs to states
    }

    fun combinations(start: String, wfs: Map<String, List<Rule>>, state: MutableMap<String, IntRange>): Long {
        var sum = 0L

        if (start == "A") {
            return state.values.map { (it.last - it.first + 1).toLong() }.reduce { acc, i -> acc * i }
        } else if (start == "R") {
            return 0
        }

        val rules = wfs.getValue(start)
        rules.dropLast(1).forEach { rule ->
            val currentRange = state.getValue(rule.varName)
            if (rule.value in currentRange) {
                if (rule.operator == '<') {
                    val newState = state.copy(rule.varName, currentRange.first..<rule.value)
                    sum += combinations(rule.ifTrue, wfs, newState)
                    state[rule.varName] = rule.value..currentRange.last
                }

                if (rule.operator == '>') {
                    val newState = state.copy(rule.varName, rule.value + 1..currentRange.last)
                    sum += combinations(rule.ifTrue, wfs, newState)
                    state[rule.varName] = currentRange.first..rule.value
                }
            }
        }

        return sum + combinations(rules.last().ifTrue, wfs, state)
    }

    fun part1(input: List<String>): Int {
        val (wfs, states) = parseInput(input)
        return states.filter { it.elaborate(wfs) == "A" }.sumOf { it.values.sum() }
    }

    fun part2(input: List<String>): Long {
        val (wfs) = parseInput(input)
        val state = listOf("x","m","a","s").associateWith { 1..4000 }.toMutableMap()
        return combinations("in", wfs, state)
    }

    val input = readInput("inputs/Day19")
    part1(input).println()
    part2(input).println()
}

fun Map<String, IntRange>.copy(varName: String, range: IntRange): MutableMap<String, IntRange> {
    val newState = toMutableMap()
    newState[varName] = range
    return newState
}

fun Map<String, Int>.elaborate(wfs: Map<String, List<Rule>>): String {
    var current = "in"

    while (current != "A" && current != "R") {
        current = wfs.getValue(current).first { it.evaluate(this) }.ifTrue
    }

    return current
}

