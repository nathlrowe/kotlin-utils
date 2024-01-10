package io.github.nathlrowe.random

import java.util.*
import kotlin.math.nextDown
import kotlin.random.*
import kotlin.random.Random

/**
 * Gets the next random `Float` from the random number generator in the specified range.
 *
 * Generates a `Float` random value uniformly distributed between the specified [from] (inclusive) and [until]
 * (exclusive) bounds.
 *
 * [from] and [until] must be finite otherwise the behavior is unspecified.
 *
 * @throws IllegalArgumentException if [from] is greater than or equal to [until].
 */
fun Random.nextFloat(from: Float, until: Float): Float {
    val size = until - from
    val r = if (size.isInfinite() && from.isFinite() && until.isFinite()) {
        val r1 = nextFloat() * (until / 2 - from / 2)
        from + r1 + r1
    } else {
        from + nextFloat() * size
    }
    return if (r >= until) until.nextDown() else r
}

/**
 * Gets the next random non-negative `Float` from the random number generator less than the specified [until] bound.
 *
 * Generates a `Float` random value uniformly distributed between 0 (inclusive) and [until] (exclusive).
 *
 * @throws IllegalArgumentException if [until] is negative or zero.
 */
fun Random.nextFloat(until: Float): Float = nextFloat(0f, until)

fun Random.nextBoolean(probability: Double = 0.5): Boolean = when {
    probability == 0.0 -> false
    probability == 1.0 -> true
    probability == 0.5 -> nextBoolean()
    probability > 0.0 && probability < 1.0 -> nextDouble() < probability
    else -> throw IllegalArgumentException("Probability value out of bounds: $probability")
}

fun Random.nextUUID(): UUID = UUID(nextLong(), nextLong())

// region Random values from ranges

// IntRange
fun IntRange.random(random: Random = Random): Int = random.nextInt(this)
fun IntRange.randomOrNull(random: Random = Random): Int? = if (isEmpty()) null else random(random)

// LongRange
fun LongRange.random(random: Random = Random): Long = random.nextLong(this)
fun LongRange.randomOrNull(random: Random = Random): Long? = if (isEmpty()) null else random(random)

// UIntRange
fun UIntRange.random(random: Random = Random): UInt = random.nextUInt(this)
fun UIntRange.randomOrNull(random: Random = Random): UInt? = if (isEmpty()) null else random(random)

// ULongRange
fun ULongRange.random(random: Random = Random): ULong = random.nextULong(this)
fun ULongRange.randomOrNull(random: Random = Random): ULong? = if (isEmpty()) null else random(random)

// endregion
