package io.github.nathlrowe.time

open class DurationUnit internal constructor(
    val name: String,
    val symbol: String?,
    internal val rawValue: Double
) : Comparable<DurationUnit> {
    init {
        require(rawValue.isFinite())
        require(rawValue != 0.0)
    }

    operator fun invoke(amount: Number = 1): Duration = Duration(amount.toDouble() * rawValue)

    operator fun times(number: Number): Duration = Duration(number.toDouble() * rawValue)

    operator fun div(unit: DurationUnit): Double = rawValue / unit.rawValue

    override fun compareTo(other: DurationUnit): Int = rawValue.compareTo(other.rawValue)

    companion object Base : DurationUnit("base", null, 1.0)
}

fun DurationUnit(name: String, symbol: String? = null, duration: Duration): DurationUnit {
    return DurationUnit(name, symbol, duration.rawValue)
}

fun Duration.toUnit(name: String, symbol: String? = null): DurationUnit {
    return DurationUnit(name, symbol, this)
}

operator fun Number.times(unit: DurationUnit): Duration = Duration(toDouble() * unit.rawValue)

// region Standard units

val SECONDS = DurationUnit.Base(1).toUnit("seconds", "s")
val MINUTES = SECONDS(60).toUnit("minutes", "m")
val HOURS = MINUTES(60).toUnit("hours", "h")
val DAYS = HOURS(24).toUnit("days", "d")
val WEEKS = DAYS(7).toUnit("weeks", "w")
val YEARS = DAYS(365.2425).toUnit("years", "y")

val MILLISECONDS = SECONDS(0.001).toUnit("milliseconds", "ms")
val MICROSECONDS = MILLISECONDS(0.001).toUnit("microseconds", "us")
val NANOSECONDS = MICROSECONDS(0.001).toUnit("nanoseconds", "ns")

val DECADES = YEARS(10).toUnit("decades")
val CENTURIES = YEARS(100).toUnit("centuries")
val MILLENNIA = YEARS(1000).toUnit("millennia")

// endregion