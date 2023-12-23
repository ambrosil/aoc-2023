import utils.Direction.*
import utils.Point
import kotlin.math.pow

fun main() {

    val directions = listOf(EAST, WEST, SOUTH, NORTH)
    val grid = mutableMapOf<Point, Char>()

    fun parseInput(input: List<String>): Point {
        var start = Point(0, 0)

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                grid[Point(x, y)] = c
                if (c == 'S') start = Point(x, y)
            }
        }

        return start
    }

    fun Point.plot(steps: Int): Int {
        var visited = mutableListOf(this)

        repeat(steps) {
            val toVisit = mutableSetOf<Point>()

            while (visited.isNotEmpty()) {
                val current = visited.removeFirst()
                directions.forEach { direction ->
                    val point = current.move(direction)
                    if (grid[point] != '#') {
                        toVisit += point
                    }
                }
            }

            visited = toVisit.toMutableList()
        }

        return visited.size
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).plot(64)
    }

    fun part2(input: List<String>): Long {
        // 26501365 = 131 * 202300 + 65
        // 65 + 131 * x for x in 0..4

        // val start = parseInput2(input)
        // start.plot(65)  -> 3906
        // start.plot(196) -> 34896
        // start.plot(327) -> 96784
        // start.plot(458) -> 189570
        // wolfram alpha -> sequence 3906 34896 96784 189570
        // f(x) = 3814 - 15357 n + 15449 n^2
        // calc for x = 202301

        val n = 202301.0
        return (3814 - 15357 * n + 15449 * n.pow(2.0)).toLong()
    }

    val input = readInput("inputs/Day21")
    part1(input).println()
    part2(input).println()
}

