fun main() {

    data class Hand(val bid: Int, val cards: String, var type: String = "") {
        fun calculateType(takeCard: (Char) -> Boolean = { true }): Hand {
            val counts = cards
                .filter { takeCard(it) }
                .fold(mutableMapOf<Char, Int>()) { acc, c ->
                    acc[c] = (acc[c] ?: 0) + 1
                    acc
                }.values

            type = if (5 in counts) {
                "five-of-a-kind"
            } else if (4 in counts) {
                "four-of-a-kind"
            } else if (3 in counts) {
                if (2 in counts) "full-house" else "three-of-a-kind"
            } else if (counts.count { it == 2 } == 2) {
                "two-pair"
            } else if (counts.count { it == 2 } == 1) {
                "one-pair"
            } else {
                "high-card"
            }

            return this
        }

        fun typeStrength(): Int {
            val strengths = mapOf(
                "high-card" to 1,
                "one-pair" to 2,
                "two-pair" to 3,
                "three-of-a-kind" to 4,
                "full-house" to 5,
                "four-of-a-kind" to 6,
                "five-of-a-kind" to 7
            )

            return strengths[this.type]!!
        }

        fun typeStrength2(): Int {
            var strength = this.typeStrength()
            val countJ = this.cards.count { it == 'J' }

            if (countJ == 5) return 7

            repeat(countJ) {
                strength += if (strength in 2..4) 2 else 1
            }

            return strength
        }
    }

    fun calculateRank(h1: Hand, h2: Hand, cardStrengths: List<Char>): Int {
        h1.cards.forEachIndexed { index, c1 ->
            val c2 = h2.cards[index]
            val s1 = cardStrengths.indexOf(c1)
            val s2 = cardStrengths.indexOf(c2)

            if (s1 > s2) {
                return 1
            } else if (s1 < s2) {
                return -1
            }
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        val cardStrengths = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
            .reversed()

        return input.map {
            val cards = it.substringBefore(" ")
            val bid = it.substringAfter(" ").toInt()
            Hand(bid, cards).calculateType()
        }.sortedWith { h1: Hand, h2: Hand ->
            val strength1 = h1.typeStrength()
            val strength2 = h2.typeStrength()

            if (strength1 != strength2) {
                strength1 - strength2
            } else {
                calculateRank(h1, h2, cardStrengths)
            }
        }.mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val cardStrengths = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
            .reversed()

        return input.map {
            val cards = it.substringBefore(" ")
            val bid = it.substringAfter(" ").toInt()
            Hand(bid, cards).calculateType { it != 'J' }
        }.sortedWith { h1: Hand, h2: Hand ->
            val strength1 = h1.typeStrength2()
            val strength2 = h2.typeStrength2()

            if (strength1 != strength2) {
                strength1 - strength2
            } else {
                calculateRank(h1, h2, cardStrengths)
            }
        }.mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.sum()
    }

    val input = readInput("inputs/Day07")
    part1(input).println()
    part2(input).println()
}

