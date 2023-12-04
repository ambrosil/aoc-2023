fun main() {

    data class Point(val x: Int, val y: Int, val value: Char) {
        fun isSymbol(): Boolean {
            return !this.value.isDigit() && this.value != '.'
        }
    }

    fun adjacents(x: Int, y: Int, matrix: List<List<Point>>): List<Point> {
        val point = matrix[y][x]
        val adjs = mutableListOf<Point>()

        if (point.x + 1 < matrix[y].size) {
            adjs.add(matrix[y][x + 1])
        }
        if (point.x - 1 >= 0) {
            adjs.add(matrix[y][x - 1])
        }
        if (point.y + 1 < matrix.size) {
            adjs.add(matrix[y + 1][x])
        }
        if (point.y - 1 >= 0) {
            adjs.add(matrix[y - 1][x])
        }
        if (point.x + 1 < matrix[y].size && point.y + 1 < matrix.size) {
            adjs.add(matrix[y + 1][x + 1])
        }
        if (point.x + 1 < matrix[y].size && point.y - 1 >= 0) {
            adjs.add(matrix[y - 1][x + 1])
        }
        if (point.x - 1 >= 0 && point.y - 1 >= 0) {
            adjs.add(matrix[y - 1][x - 1])
        }
        if (point.x - 1 >= 0 && point.y + 1 < matrix.size) {
            adjs.add(matrix[y + 1][x - 1])
        }

        return adjs
    }

    fun checkAdjacents(x: Int, y: Int, matrix: List<List<Point>>, block: (p : Point) -> Boolean): Boolean {
        val point = matrix[y][x]
        if (!point.value.isDigit()) return true

        if (point.x + 1 < matrix[y].size) {
            val adjPoint = matrix[y][x + 1]
            if (block(adjPoint)) return true
        }
        if (point.x - 1 >= 0) {
            val adjPoint = matrix[y][x - 1]
            if (block(adjPoint)) return true
        }
        if (point.y + 1 < matrix.size) {
            val adjPoint = matrix[y + 1][x]
            if (block(adjPoint)) return true
        }
        if (point.y - 1 >= 0) {
            val adjPoint = matrix[y - 1][x]
            if (block(adjPoint)) return true
        }
        if (point.x + 1 < matrix[y].size && point.y + 1 < matrix.size) {
            val adjPoint = matrix[y + 1][x + 1]
            if (block(adjPoint)) return true
        }
        if (point.x + 1 < matrix[y].size && point.y - 1 >= 0) {
            val adjPoint = matrix[y - 1][x + 1]
            if (block(adjPoint)) return true
        }
        if (point.x - 1 >= 0 && point.y - 1 >= 0) {
            val adjPoint = matrix[y - 1][x - 1]
            if (block(adjPoint)) return true
        }
        if (point.x - 1 >= 0 && point.y + 1 < matrix.size) {
            val adjPoint = matrix[y + 1][x - 1]
            if (block(adjPoint)) return true
        }

        return false
    }

    fun isGear(point : Point, matrix: List<List<Point>>): Boolean {
        val adjacents = adjacents(point.x, point.y, matrix)
        return true // adjacents
    }

    fun isPartNumber(x: Int, y: Int, matrix: List<List<Point>>): Boolean {
        return checkAdjacents(x, y, matrix) { it.isSymbol() }
    }

    fun List<Point>.partNumbers(): Map<Int, List<Point>> {
        var counter = 0
        return this.filter { it.value.isDigit() }.groupBy {
            val index = this.indexOf(it)

            if (index - 1 >= 0) {
                val adjPoint = this[index - 1]
                if (!adjPoint.value.isDigit() || it.x - 1 != adjPoint.x) {
                    ++counter
                }
            }

            counter
        }
    }

    fun part1(input: List<String>): Int {
        val matrix = input.mapIndexed { y, line ->
            line.mapIndexed { x, value ->
                Point(x, y, value)
            }
        }

        val partNumbers = mutableListOf<Int>()
        matrix.forEach { line ->
            line.partNumbers().values
                .filterNot { it.all { p -> !isPartNumber(p.x, p.y, matrix) } }
                .map { it.joinToString(separator = "") { p -> p.value.toString() } }
                .map { it.toInt() }
                .forEach { partNumbers.add(it) }
        }

        return partNumbers.sum()
    }

    fun isGear(it: Point): Boolean {
        return true // it.adjPointSize() == 2
    }

    fun part2(input: List<String>): Int {
        val matrix = input.mapIndexed { y, line ->
            line.mapIndexed { x, value ->
                Point(x, y, value)
            }
        }

        matrix.forEach { line ->
            line.filter { it.value == '*' }
                //.filter { it.isGear() }
        }

        return 1
    }

    val input = readInput("inputs/Day03")
    part1(input).println()
    part2(input).println()
}


