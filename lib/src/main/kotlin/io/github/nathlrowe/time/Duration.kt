package io.github.nathlrowe.time

import io.github.nathlrowe.math.toLong
import java.math.RoundingMode
import kotlin.random.Random

@JvmInline
value class Duration internal constructor(
    internal val rawValue: Double
) : Comparable<Duration> {
    constructor(amount: Number, unit: DurationUnit) : this(amount.toDouble() * unit.rawValue)

    init {
        require(!rawValue.isNaN())
    }

    fun toDouble(unit: DurationUnit = DurationUnit.Base): Double = rawValue / unit.rawValue

    fun toLong(
        unit: DurationUnit = DurationUnit.Base,
        roundingMode: RoundingMode = RoundingMode.FLOOR
    ): Long = toDouble(unit).toLong(roundingMode)

    operator fun plus(other: Duration): Duration = Duration(rawValue + other.rawValue)

    operator fun minus(other: Duration): Duration = Duration(rawValue - other.rawValue)

    operator fun times(number: Number): Duration = Duration(rawValue * number.toDouble())

    operator fun div(number: Number): Duration = Duration(rawValue / number.toDouble())

    operator fun div(other: Duration): Double = rawValue / other.rawValue

    operator fun div(unit: DurationUnit): Double = rawValue / unit.rawValue

    operator fun rem(other: Duration): Duration = Duration(rawValue.rem(other.rawValue))

    fun mod(other: Duration): Duration = Duration(rawValue.mod(other.rawValue))

    operator fun unaryMinus(): Duration = Duration(-rawValue)

    override fun compareTo(other: Duration): Int = rawValue.compareTo(other.rawValue)

    override fun toString(): String = "Duration($rawValue)"

    companion object {
        val ZERO = Duration(0.0)

        val MIN_VALUE = Duration(Double.MIN_VALUE)
        val MAX_VALUE = Duration(Double.MAX_VALUE)

        val NEGATIVE_INFINITY = Duration(Double.NEGATIVE_INFINITY)
        val POSITIVE_INFINITY = Duration(Double.POSITIVE_INFINITY)
    }
}

val Duration.isInfinite get() = rawValue.isInfinite()
val Duration.isFinite get() = rawValue.isFinite()

operator fun Number.times(duration: Duration): Duration = duration * this

fun Duration(
    years: Number = 0,
    weeks: Number = 0,
    days: Number = 0,
    hours: Number = 0,
    minutes: Number = 0,
    seconds: Number = 0,
    milliseconds: Number = 0,
    microseconds: Number = 0,
    nanoseconds: Number = 0
): Duration {
    var result = Duration.ZERO
    if (years != 0) result += years.years
    if (weeks != 0) result += weeks.weeks
    if (days != 0) result += days.days
    if (hours != 0) result += hours.hours
    if (minutes != 0) result += minutes.minutes
    if (seconds != 0) result += seconds.seconds
    if (milliseconds != 0) result += milliseconds.milliseconds
    if (microseconds != 0) result += microseconds.microseconds
    if (nanoseconds != 0) result += nanoseconds.nanoseconds
    return result
}


val Number.nanoseconds get() = Duration(this, NANOSECONDS)
val Number.microseconds get() = Duration(this, MICROSECONDS)
val Number.milliseconds get() = Duration(this, MILLISECONDS)
val Number.seconds get() = Duration(this, SECONDS)
val Number.minutes get() = Duration(this, MINUTES)
val Number.hours get() = Duration(this, HOURS)
val Number.days get() = Duration(this, DAYS)
val Number.weeks get() = Duration(this, WEEKS)
val Number.years get() = Duration(this, YEARS)

val Duration.inNanoseconds get() = toDouble(NANOSECONDS)
val Duration.inMicroseconds get() = toDouble(MICROSECONDS)
val Duration.inMilliseconds get() = toDouble(MILLISECONDS)
val Duration.inSeconds get() = toDouble(SECONDS)
val Duration.inMinutes get() = toDouble(MINUTES)
val Duration.inHours get() = toDouble(HOURS)
val Duration.inDays get() = toDouble(DAYS)
val Duration.inWeeks get() = toDouble(WEEKS)
val Duration.inYears get() = toDouble(YEARS)

val Duration.inWholeNanoseconds get() = toLong(NANOSECONDS)
val Duration.inWholeMicroseconds get() = toLong(MICROSECONDS)
val Duration.inWholeMilliseconds get() = toLong(MILLISECONDS)
val Duration.inWholeSeconds get() = toLong(SECONDS)
val Duration.inWholeMinutes get() = toLong(MINUTES)
val Duration.inWholeHours get() = toLong(HOURS)
val Duration.inWholeDays get() = toLong(DAYS)
val Duration.inWholeWeeks get() = toLong(WEEKS)
val Duration.inWholeYears get() = toLong(YEARS)


fun Random.nextDuration(from: Duration = Duration.ZERO, until: Duration): Duration =
    Duration(nextDouble(from.rawValue, until.rawValue))

