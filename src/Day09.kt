fun main() {

    fun steps(it: String): MutableList<List<Int>> {
        var list = it.split(" ").map { it.toInt() }
        val steps = mutableListOf<List<Int>>()
        while (list.count { it == 0 } != list.size) {
            steps += list
            list = list.windowed(2).map { it.last() - it.first() }
        }
        return steps
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { steps(it).sumOf { it.last() } }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            steps(it).reversed().fold(0) { acc, i -> i.first() - acc }.toInt()
        }
    }

    val input = readInput("inputs/Day09")
    part1(input).println()
    part2(input).println()
}

