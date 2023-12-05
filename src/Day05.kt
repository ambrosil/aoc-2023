fun main() {

    data class Conversion(val destination: Long, val source: Long, val length: Long)
    data class PlantMap(val type: String, val conversions: List<Conversion>)

    fun parseInput(input: List<String>): Pair<List<Long>, List<PlantMap>> {
        val seeds = input.first().substringAfter("seeds: ").split(" ").map { it.toLong() }

        var current = ""
        val maps = input
            .takeLast(input.size - 2)
            .groupBy {
                if (it.contains("map:")) current = it
                current
            }.map { e ->
                val type = e.key.substringBefore(" map:")
                val conversions = e.value
                    .filterIndexed { i, line -> i > 0 && line.isNotBlank() }
                    .map { s -> s.split(" ").map { it.toLong() } }
                    .map { Conversion(it[0], it[1], it[2]) }

                PlantMap(type, conversions)
            }

        return seeds to maps
    }

    fun process(item: Long, plantMap: PlantMap): Long {
        val conversion = plantMap.conversions.find { item in it.source..it.source + it.length }
        if (conversion != null) {
            return conversion.destination - conversion.source + item
        }

        return item
    }

    fun location(seed: Long, maps: List<PlantMap>): Long {
        return maps.fold(seed) { acc, item -> process(acc, item) }
    }

    fun part1(input: List<String>): Long {
        val (seeds, maps) = parseInput(input)
        return seeds.minOf { location(it, maps) }
    }

    fun part2(input: List<String>): Long {
        val (seeds, maps) = parseInput(input)
        return seeds
            .chunked(2)
            .map { (start, length) -> start until start + length }
            .minOf { s -> s.minOf { location(it, maps) }
        }
    }

    val input = readInput("inputs/Day05")
    part1(input).println()
    part2(input).println()
}

