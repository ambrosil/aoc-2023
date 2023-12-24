import utils.Direction
import utils.Direction.*
import utils.PointL

fun main() {
    data class Command(val direction: Direction, val amount: Int, val hexColor: String = "")

    fun mapDirection(direction: String): Direction {
        return when (direction) {
            "R" -> EAST
            "L" -> WEST
            "U" -> NORTH
            "D" -> SOUTH
            else -> error("Cannot map direction $direction")
        }
    }

    fun parseInput(input: List<String>): List<Command> {
        return input.map {
            val (direction, amount) = it.split(" ")
            Command(mapDirection(direction), amount.toInt())
        }
    }

    fun parseInput2(input: List<String>): List<Command> {
        return input.map {
            val line = it.substringAfter("(").substringBefore(")")
            val direction = listOf(EAST, SOUTH, WEST, NORTH)[line.last().digitToInt()]
            val distance = Integer.parseInt(line.drop(1).dropLast(1), 16)
            Command(direction, distance)
        }
    }

    fun shoelace(commands: List<Command>): Long {
        var current = PointL(0, 0)
        val points = mutableListOf(current)
        var perimeter = 0L
        var area = 0L

        commands.forEach { cmd ->
            current = current.move(cmd.direction, cmd.amount.toLong())
            perimeter += cmd.amount
            area += points.last().x * current.y - current.x * points.last().y
            points += current
        }

        return area / 2 - perimeter / 2 + 1 + perimeter
    }

    fun part1(input: List<String>): Long {
        return shoelace(parseInput(input))
    }

    fun part2(input: List<String>): Long {
        return shoelace(parseInput2(input))
    }

    val input = readInput("inputs/Day18")
    part1(input).println()
    part2(input).println()
}

