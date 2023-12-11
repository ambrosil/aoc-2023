import utils.Point
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun parseInput(input: List<String>): MutableList<Point> {
        val galaxies = mutableListOf<Point>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxies += Point(x, y)
                }
            }
        }

        return galaxies
    }

    fun solve(input: List<String>, expandSize: Long = 1L): Long {
        val galaxies = parseInput(input)
        val expandedY = input.indices.filter { y -> (0..<input[0].length).none { x -> Point(x, y) in galaxies } }
        val expandedX = (0..<input[0].length).filter { x -> input.indices.none { y -> Point(x, y) in galaxies } }

        return galaxies
            .flatMapIndexed { index, from -> galaxies.drop(index + 1).map { from to it } }
            .sumOf { (from, to) ->
                val expX = (min(from.x, to.x)..max(from.x, to.x)).count { it in expandedX }
                val expY = (min(from.y, to.y)..max(from.y, to.y)).count { it in expandedY }
                from.manhattan(to) + (expX + expY) * (expandSize - 1)
            }
    }

    fun part1(input: List<String>) = solve(input, 2)

    fun part2(input: List<String>) = solve(input, 1_000_000L)

    val input = readInput("inputs/Day11")
    part1(input).println()
    part2(input).println()
}

