package io.github.nathlrowe.time

import io.github.nathlrowe.utils.Interval
import io.github.nathlrowe.utils.Openness
import kotlin.random.Random

class InstantInterval internal constructor(
    override val min: Instant,
    override val max: Instant,
    override val openness: Openness = Openness.CLOSED_OPEN
) : Interval<Instant> {
    val duration get() = max - min

    companion object {
        val EMPTY = InstantInterval(Instant(Duration(1.0)), Instant(Duration(0.0)))

        fun between(
            min: Instant,
            max: Instant,
            openness: Openness = Openness.CLOSED_OPEN
        ): InstantInterval = InstantInterval(min, max, openness)

        fun from(
            start: Instant,
            duration: Duration,
            openness: Openness = Openness.CLOSED_OPEN
        ): InstantInterval = InstantInterval(start, start + duration, openness)

        fun to(
            end: Instant,
            duration: Duration,
            openness: Openness = Openness.CLOSED_OPEN
        ): InstantInterval = InstantInterval(end - duration, end, openness)
    }
}

fun Random.nextInstant(interval: Interval<Instant>): Instant = nextInstant(interval.min, interval.max)

fun InstantInterval.random(random: Random = Random) = random.nextInstant(this)
