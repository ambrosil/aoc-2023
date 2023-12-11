import utils.Direction.*
import utils.Point

fun main() {

    val pipes = mapOf(
        '-' to listOf(EAST, WEST),
        '|' to listOf(SOUTH, NORTH),
        'L' to listOf(NORTH, EAST),
        'J' to listOf(NORTH, WEST),
        '7' to listOf(SOUTH, WEST),
        'F' to listOf(SOUTH, EAST),
        'S' to listOf(NORTH, SOUTH, WEST, EAST)
    )

    fun parseInput(input: List<String>): MutableMap<Point, Char> {
        val grid = mutableMapOf<Point, Char>()
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                grid[Point(x, y)] = c
            }
        }

        return grid
    }

    fun part1(input: List<String>): Int {
        val matrix = parseInput(input)

        val start = matrix.entries.first { it.value == 'S' }.key
        val visited = mutableListOf(start to 0)
        val distances = mutableMapOf(start to 0)

        while (visited.isNotEmpty()) {
            val (current, distance) = visited.removeFirst()
            distances[current] = distance

            pipes[matrix[current]]!!.forEach { direction ->
                val point = current.move(direction)
                val notDot = matrix[point] != '.'
                val isLinkedToPrev = direction.reverse() in (pipes[matrix[point]] ?: listOf())
                val inGrid = point in matrix.keys
                val alreadyVisited = point in distances.keys

                if (!alreadyVisited && inGrid && notDot && isLinkedToPrev) {
                    visited += point to (distance + 1)
                }
            }
        }

        return distances.values.max()
    }

    fun part2(input: List<String>): Int {
        return 1
    }

    val input = readInput("inputs/Day10")
    part1(input).println()
    part2(input).println()
}

