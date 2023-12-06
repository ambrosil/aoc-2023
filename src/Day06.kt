fun main() {

    fun parseInput(input: List<String>): List<Pair<Long, Long>> {
        val read = { s: String -> s.substringAfter(":").split(" ").filter { it.isNotEmpty() }.map { it.toLong() } }
        return read(input.first()).zip(read(input.last()))
    }

    fun countWins(time: Long, distance: Long): Int {
        return (0L..time).map { it * (time - it) }.count { it > distance }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input)
            .map { (time, distance) -> countWins(time, distance) }
            .reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val (time, distance) = input.map { it.substringAfter(" ").replace(" ", "").toLong() }
        return countWins(time, distance)
    }

    val input = readInput("inputs/Day06")
    part1(input).println()
    part2(input).println()
}

