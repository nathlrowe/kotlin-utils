package io.github.nathlrowe.time

import io.github.nathlrowe.utils.Openness
import kotlin.random.Random

@JvmInline
value class Instant internal constructor(val fromEpoch: Duration) : Comparable<Instant> {
    operator fun plus(duration: Duration): Instant = Instant(fromEpoch + duration)

    operator fun minus(duration: Duration): Instant = Instant(fromEpoch - duration)

    operator fun minus(other: Instant): Duration = fromEpoch - other.fromEpoch

    operator fun rangeTo(other: Instant): InstantInterval = InstantInterval(this, other, Openness.CLOSED_CLOSED)

    infix fun until(other: Instant): InstantInterval = InstantInterval(this, other, Openness.CLOSED_OPEN)

    override fun compareTo(other: Instant): Int = fromEpoch.rawValue.compareTo(other.fromEpoch.rawValue)

    override fun toString(): String = "Instant(${fromEpoch.rawValue})"

    companion object {
        val ZERO = Instant(Duration.ZERO)
        val EPOCH = ZERO

        val NEGATIVE_INFINITY = Instant(Duration.NEGATIVE_INFINITY)
        val POSITIVE_INFINITY = Instant(Duration.POSITIVE_INFINITY)

        fun fromEpoch(duration: Duration) = Instant(duration)

        fun fromEpoch(amount: Number, unit: DurationUnit) = Instant(Duration(amount, unit))
    }
}

val Instant.isInfinite get() = fromEpoch.isInfinite
val Instant.isFinite get() = fromEpoch.isFinite

operator fun Duration.plus(instant: Instant): Instant = instant + this

fun Random.nextInstant(from: Instant, until: Instant): Instant =
    Instant(nextDuration(from.fromEpoch, until.fromEpoch))
