import utils.Direction
import utils.Direction.*
import utils.Point
import kotlin.math.max

fun main() {

    val grid = mutableMapOf<Point, Char>()
    lateinit var start: Point
    lateinit var end: Point

    fun parseInput(input: List<String>): MutableMap<Point, Char> {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                grid[Point(x, y)] = c
                if (y == 0 && c == '.') start = Point(x,y)
                if (input.lastIndex == y && c == '.') end = Point(x,y)
            }
        }

        return grid
    }

    fun traverse(adjacents: (Point) -> List<Pair<Point, Int>>): Int {
        var max = 0
        val visited = mutableSetOf<Point>()

        fun traverseInner(point: Point, steps: Int):Int {
            if (point == end) {
                max = max(steps, max)
                return max
            }
            visited += point
            adjacents(point)
                .filter { (place) -> place !in visited }
                .forEach { (place, distance) -> traverseInner(place, distance + steps) }
            visited -= point
            return max
        }

        return traverseInner(start, 0)
    }

    fun Map<Point, Char>.nodes(): MutableSet<Point> {
        val points = mutableSetOf<Point>()
        points.add(start)
        points.add(end)

        this.entries.forEach { entry ->
            if (entry.value != '#') {
                entry.key.apply {
                    val isNode = getAdjacentSides().filter { it in grid }.filter { grid[it] != '#' }.size > 2
                    if (isNode) points.add(this)
                }
            }
        }

        return points
    }

    fun calcDistance(from: Point, otherPoints: Set<Point>): Map<Point, Int> {
        val queue = ArrayDeque<Pair<Point, Int>>().apply {
            add(from to 0)
        }

        val seen = mutableSetOf(from)
        val answer = mutableMapOf<Point, Int>()
        while (queue.isNotEmpty()) {
            val (location, distance) = queue.removeFirst()
            if (location != from && location in otherPoints) {
                answer[location] = distance
            } else {
                location.getAdjacentSides()
                    .filter { it in grid }
                    .filter { grid[it] != '#' }
                    .filter { it !in seen }
                    .forEach {
                        seen += it
                        queue.add(it to distance + 1)
                    }
            }
        }

        return answer
    }

    fun simplifyGrid(): Map<Point, Map<Point, Int>> =
        grid.nodes().let { node ->
            node.associateWith {
                point -> calcDistance(point, node)
            }
        }

    fun Char.matchesDirection(direction: Direction?) =
        when (this) {
            '^' -> NORTH == direction
            '<' -> WEST == direction
            'v' -> SOUTH == direction
            '>' -> EAST == direction
            '.' -> true
            else -> false
        }

    fun part1(input: List<String>): Int {
        parseInput(input)

        return traverse { point ->
            point.getAdjacentSides()
                .filter { it in grid }
                .filter { newPoint -> grid[newPoint]!!.matchesDirection(point.getDirection(newPoint)) }
                .map { it to 1 }
        }
    }

    fun part2(input: List<String>): Int {
        parseInput(input)

        val newGrid = simplifyGrid()
        return traverse { newGrid[it]!!.map { e -> e.key to e.value } }
    }

    val input = readInput("inputs/Day23")
    part1(input).println()
    part2(input).println()
}

