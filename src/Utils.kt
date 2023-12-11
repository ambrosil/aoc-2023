import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun String.allInts() = allIntsInString(this)
fun allIntsInString(line: String): List<Int> {
    return """-?\d+""".toRegex().findAll(line)
        .map { it.value.toInt() }
        .toList()
}

fun <T> List<T>.nth(n: Int): T =
    this[n % size]

inline fun <T> List<T>.indexOfLast(predicate: (T) -> Boolean): Int {
    for (i in this.indices.reversed()) {
        if (predicate(this[i])) {
            return i
        }
    }

    return -1
}
