fun main() {

    fun parseInput(input: List<String>): List<List<List<Int>>> {
        return input.map {
            it.split(": ").last()
                .split(" | ")
                .map { it.split(" ") }
                .map { it.filter { it.isNotBlank() } }
                .map { it.map { it.toInt() } }
        }
    }

    fun part1(input: List<String>): Int {
        val cards = parseInput(input)

        return cards.sumOf { card ->
            val winning = card.first()
            val current = card.last()

            val matches = current.filter { it in winning }
            when (matches.size) {
                0 -> 0L.toInt()
                1 -> 1L.toInt()
                else -> matches.takeLast(matches.size - 1).fold(1) { acc, _ -> acc * 2 }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cards = parseInput(input)
        val map = mutableMapOf<Int, Int>()

        cards.forEachIndexed { index, card ->
            val winning = card.first()
            val current = card.last()
            map[index + 1] = current.count { it in winning }
        }

        val instances = mutableMapOf<Int, Int>()
        map.toMap(instances)
        map.keys.forEach { instances[it] = 1 }

        map.entries.forEach { entry ->
            repeat(instances[entry.key]!!) {
                for (index in 0 until entry.value) {
                    instances[entry.key + index + 1] = instances[entry.key + index + 1]!! + 1
                }
            }
        }

        return instances.values.sum()
    }

    val input = readInput("inputs/Day04")
    part1(input).println()
    part2(input).println()
}

