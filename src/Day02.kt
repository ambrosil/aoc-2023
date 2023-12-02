fun main() {

    data class GameSet(var blue: Int, var red: Int, var green: Int)
    data class Game(var id: Int, var sets : List<GameSet>)

    fun parseInput(input: List<String>): List<Game> {
        return input.mapIndexed { index, line ->
            val substring = line.substring(line.indexOf(": ") + 2)
            val sets = substring.split("; ").map {
                val colors = it.split(", ").associate {
                    val values = it.split(" ")
                    val n = values.first().toInt()
                    val color = values.last()
                    color to n
                }

                val red = colors["red"] ?: 0
                val blue = colors["blue"] ?: 0
                val green = colors["green"] ?: 0
                GameSet(blue, red, green)
            }

            Game(index + 1, sets)
        }
    }

    fun part1(input: List<String>): Int {
        val reds = 12
        val greens = 13
        val blues = 14

        return parseInput(input)
            .filter { it.sets.all { s -> s.red <= reds && s.blue <= blues && s.green <= greens } }
            .sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).sumOf { game ->
            game.sets.maxOf { it.green } *
            game.sets.maxOf { it.blue } *
            game.sets.maxOf { it.red }
        }
    }

    val input = readInput("inputs/Day02")
    part1(input).println()
    part2(input).println()
}

