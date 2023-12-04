fun main() {

    data class Point(val x: Int, val y: Int, val value: Char) {
        fun isSymbol(): Boolean {
            return !this.value.isDigit() && this.value != '.'
        }
    }

    fun adjacents(x: Int, y: Int, matrix: List<List<Point>>): List<Point> {
        val point = matrix[y][x]
        val adjacents = mutableListOf<Point>()

        if (point.x - 1 >= 0 && point.y - 1 >= 0) {
            adjacents.add(matrix[y - 1][x - 1])
        }
        if (point.y - 1 >= 0) {
            adjacents.add(matrix[y - 1][x])
        }
        if (point.x + 1 < matrix[y].size && point.y - 1 >= 0) {
            adjacents.add(matrix[y - 1][x + 1])
        }

        if (point.x - 1 >= 0) {
            adjacents.add(matrix[y][x - 1])
        }
        if (point.x + 1 < matrix[y].size) {
            adjacents.add(matrix[y][x + 1])
        }

        if (point.x - 1 >= 0 && point.y + 1 < matrix.size) {
            adjacents.add(matrix[y + 1][x - 1])
        }
        if (point.y + 1 < matrix.size) {
            adjacents.add(matrix[y + 1][x])
        }
        if (point.x + 1 < matrix[y].size && point.y + 1 < matrix.size) {
            adjacents.add(matrix[y + 1][x + 1])
        }

        return adjacents
    }

    fun checkAdjacents(x: Int, y: Int, matrix: List<List<Point>>, block: (p : Point) -> Boolean): Boolean {
        val point = matrix[y][x]
        if (!point.value.isDigit()) return true
        return adjacents(x, y, matrix).any { block(it) }
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

    val visited = mutableListOf<Point>()

    fun numberify(matrix: List<List<Point>>, point: Point): Int {
        var number = point.value.toString()
        var prevDone = false
        var nextDone = false

        visited += point

        for (i in 1 until matrix[point.y].size) {
            if (point.x - i >= 0 && !prevDone) {
                val v = matrix[point.y][point.x - i]

                if (v.value.isDigit()) {
                    if (v in visited) return -1
                    visited += v

                    number = v.value + number
                } else {
                    prevDone = true
                }
            }

            if (point.x + i < matrix[point.y].size && !nextDone) {
                val v = matrix[point.y][point.x + i]

                if (v.value.isDigit()) {
                    if (v in visited) return -1
                    visited += v

                    number = number + v.value
                } else {
                    nextDone = true
                }
            }
        }

        return number.toInt()
    }

    fun gear(point : Point, matrix: List<List<Point>>): Int {
        val adjacents = adjacents(point.x, point.y, matrix)
            .filter { it.value.isDigit() }
            .map { numberify(matrix, it) }
            .filter { it >= 0 }

        if (adjacents.size == 2) {
            return adjacents[0] * adjacents[1]
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val matrix = input.mapIndexed { y, line ->
            line.mapIndexed { x, value ->
                Point(x, y, value)
            }
        }

        return matrix.sumOf { line ->
            line.filter { it.value == '*' }.sumOf { gear(it, matrix) }
        }
    }

    val input = readInput("inputs/Day03")
    part1(input).println()
    part2(input).println()
}


