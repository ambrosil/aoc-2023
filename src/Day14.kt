import utils.Direction
import utils.Direction.*
import utils.Point

fun main() {

    fun Array<CharArray>.exist(at: Point) = at.y in this.indices && at.x in this[at.y].indices
    operator fun Array<CharArray>.set(at: Point, c: Char) { this[at.y][at.x] = c }
    operator fun Array<CharArray>.get(at: Point): Char = this[at.y][at.x]

    fun Array<CharArray>.draw() {
        for (y in indices) {
            for (x in this[y].indices) {
                print(this[y][x])
            }
            println("")
        }

        println("------------------------")
    }

    fun Array<CharArray>.swap(p1: Point, p2: Point) {
        val tmp = this[p1]
        this[p1] = this[p2]
        this[p2] = tmp
    }

    fun Array<CharArray>.rocksFrom(direction: Direction): List<Point> {
        return when (direction) {
            NORTH -> indices.flatMap { y -> first().indices.map { x -> Point(x, y) } }
            WEST  -> first().indices.flatMap { x -> indices.map { y -> Point(x, y) } }
            EAST  -> first().indices.reversed().flatMap { x -> indices.map { y -> Point(x, y) } }
            SOUTH -> indices.reversed().flatMap { y -> first().indices.map { x -> Point(x, y) } }
        }.filter { this[it] == 'O' }
    }

    fun Array<CharArray>.simulate(p: Point, direction: Direction) {
        var current = p
        while (exist(current.move(direction)) && this[current.move(direction)] == '.') {
            current.move(direction).let {
                swap(current, it)
                current = it
            }
        }
    }

    fun Array<CharArray>.tilt(direction: Direction): Array<CharArray> {
        rocksFrom(direction).forEach { simulate(it, direction) }
        return this
    }

    fun Array<CharArray>.cycle(): Array<CharArray> {
        listOf(NORTH, WEST, SOUTH, EAST).forEach { tilt(it) }
        return this
    }

    fun Array<CharArray>.load() = mapIndexed { y, row ->
        row.sumOf { c -> if (c == 'O') size - y else 0 }
    }.sum()

    fun part1(input: List<String>): Int {
        return input
            .map { it.toCharArray() }
            .toTypedArray<CharArray>()
            .tilt(NORTH)
            .load()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val visited = mutableMapOf<Int, Int>()
        val target = 1_000_000_000
        var cycleNum = 1

        while (cycleNum <= target) {
            grid.cycle()
            when (val state = grid.sumOf { it.joinToString("").hashCode() }) {
                !in visited -> visited[state] = cycleNum++
                else -> {
                    val cycleLength = cycleNum - visited[state]!!
                    val cyclesLeft = (target - cycleNum) % cycleLength
                    repeat(cyclesLeft) {
                        grid.cycle()
                    }
                    return grid.load()
                }
            }
        }

        return grid.load()
    }

    val input = readInput("inputs/Day14")
    part1(input).println()
    part2(input).println()
}

