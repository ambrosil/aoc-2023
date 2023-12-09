import utils.leastCommonMultiple

fun main() {

    data class Node(val left: String, val right: String)

    fun parseInput(input: List<String>): Pair<String, MutableMap<String, Node>> {
        val directions = input.first()
        val network = input.drop(2).fold(mutableMapOf<String, Node>()) { acc, i ->
            val nodeName = i.split(" = ").first().trim()
            val (left, right) = i.replace("(", "")
                .replace(")", "")
                .split(" = ")
                .last()
                .split(", ")

            acc[nodeName] = Node(left, right)
            acc
        }

        return directions to network
    }

    fun findZ(startNode: String, directions: String, network: MutableMap<String, Node>, condition: (current: String) -> Boolean): Long {
        var counter = 0L
        var current = startNode

        while (condition(current)) {
            directions.forEach { current = if (it == 'R') network[current]!!.right else network[current]!!.left }
            counter += directions.length
        }

        return counter
    }

    fun part1(input: List<String>): Long {
        val (directions, network) = parseInput(input)
        return findZ("AAA", directions, network) { it != "ZZZ" }
    }

    fun part2(input: List<String>): Any {
        val (directions, network) = parseInput(input)
        return network.keys.filter { it.endsWith("A") }
            .map { findZ(it, directions, network) { !it.endsWith("Z") } }
            .reduce { acc, i -> leastCommonMultiple(acc, i) }
    }

    val input = readInput("inputs/Day08")
    part1(input).println()
    part2(input).println()
}

