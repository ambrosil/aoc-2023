fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { it.filter { c -> c.isDigit() } }
            .sumOf { "${it.first()}${it.last()}".toInt() }
    }

    fun part2(input: List<String>): Int {
        val spellingMap = mapOf(
            "one" to "1", "two" to "2",
            "three" to "3", "four" to "4",
            "five" to "5", "six" to "6",
            "seven" to "7", "eight" to "8", "nine" to "9"
        )

        fun String.fixSpelling(): String {
            var res = ""

            this.forEachIndexed { i, c ->
                if (c.isDigit()) res += c

                spellingMap.entries.forEach {
                    if (this.length >= i + it.key.length) {
                        val substring = this.substring(i, i + it.key.length)
                        if (substring == it.key) res += it.value
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

