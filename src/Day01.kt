fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.filter { c -> c.isDigit() } }
            .sumOf { "${it.first()}${it.last()}".toInt() }
    }

    fun part2(input: List<String>): Int {
        val words = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        fun String.fixSpelling(): String {
            var res = ""

            this.forEachIndexed { i, c ->
                if (c.isDigit()) res += c

                words.forEachIndexed { index, word ->
                    if (this.length >= i + word.length) {
                        val substring = this.substring(i, i + word.length)
                        if (substring == word) res += index + 1
                    }
                }
            }

            return res
        }

        return input
            .map { it.fixSpelling().filter { c -> c.isDigit() } }
            .sumOf { "${it.first()}${it.last()}".toInt() }
    }

    val input = readInput("inputs/Day01")
    part1(input).println()
    part2(input).println()
}

