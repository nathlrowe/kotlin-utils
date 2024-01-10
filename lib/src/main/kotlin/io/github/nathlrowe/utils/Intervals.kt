package io.github.nathlrowe.utils

interface Interval<T : Comparable<T>> {
    val min: T
    val max: T
    val openness: Openness

    @Suppress("ConvertTwoComparisonsToRangeCheck")
    operator fun contains(value: T): Boolean = when (openness) {
        Openness.OPEN_OPEN -> value > min && value < max
        Openness.OPEN_CLOSED -> value > min && value <= max
        Openness.CLOSED_OPEN -> value >= min && value < max
        Openness.CLOSED_CLOSED -> value >= min && value <= max
    }

    fun isEmpty(): Boolean = when (openness) {
        Openness.OPEN_OPEN -> min <= max
        else -> min < max
    }
}

// Basic constructors:
fun Interval(min: Char, max: Char, openness: Openness = DEFAULT_OPENNESS) = CharInterval(min, max, openness)
fun Interval(min: Int, max: Int, openness: Openness = DEFAULT_OPENNESS) = IntInterval(min, max, openness)
fun Interval(min: Long, max: Long, openness: Openness = DEFAULT_OPENNESS) = LongInterval(min, max, openness)
fun Interval(min: Float, max: Float, openness: Openness = DEFAULT_OPENNESS) = FloatInterval(min, max, openness)
fun Interval(min: Double, max: Double, openness: Openness = DEFAULT_OPENNESS) = DoubleInterval(min, max, openness)

operator fun Float.rangeTo(other: Float) = Interval(this, other, Openness.CLOSED_CLOSED)
operator fun Float.rangeUntil(other: Float) = Interval(this, other, Openness.CLOSED_OPEN)
infix fun Float.until(other: Float) = this..<other

operator fun Double.rangeTo(other: Double) = Interval(this, other, Openness.CLOSED_CLOSED)
operator fun Double.rangeUntil(other: Double) = Interval(this, other, Openness.CLOSED_OPEN)
infix fun Double.until(other: Double) = this..<other

// region Implementations

class CharInterval(
    override val min: Char,
    override val max: Char,
    override val openness: Openness = DEFAULT_OPENNESS
) : Interval<Char> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CharInterval) return false
        if (isEmpty() && other.isEmpty()) return true
        return min == other.min && max == other.max && openness == other.openness
    }

    override fun hashCode(): Int {
        if (isEmpty()) return -1
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        result = 31 * result + openness.hashCode()
        return result
    }

    override fun toString(): String = openness.formatIntervalString(min, max)

    companion object {
        val EMPTY = CharInterval(Char(1), Char(0))
    }
}

class IntInterval(
    override val min: Int,
    override val max: Int,
    override val openness: Openness = DEFAULT_OPENNESS
) : Interval<Int> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IntInterval) return false
        if (isEmpty() && other.isEmpty()) return true
        return min == other.min && max == other.max && openness == other.openness
    }

    override fun hashCode(): Int {
        if (isEmpty()) return -1
        var result = min
        result = 31 * result + max
        result = 31 * result + openness.hashCode()
        return result
    }

    override fun toString(): String = openness.formatIntervalString(min, max)

    companion object {
        val EMPTY = IntInterval(1, 0)
    }
}

class LongInterval(
    override val min: Long,
    override val max: Long,
    override val openness: Openness = DEFAULT_OPENNESS
) : Interval<Long> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LongInterval) return false
        if (isEmpty() && other.isEmpty()) return true
        return min == other.min && max == other.max && openness == other.openness
    }

    override fun hashCode(): Int {
        if (isEmpty()) return -1
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        result = 31 * result + openness.hashCode()
        return result
    }

    override fun toString(): String = openness.formatIntervalString(min, max)

    companion object {
        val EMPTY = LongInterval(1L, 0L)
    }
}

class FloatInterval(
    override val min: Float,
    override val max: Float,
    override val openness: Openness = DEFAULT_OPENNESS
) : Interval<Float> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FloatInterval) return false
        if (isEmpty() && other.isEmpty()) return true
        return min == other.min && max == other.max && openness == other.openness
    }

    override fun hashCode(): Int {
        if (isEmpty()) return -1
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        result = 31 * result + openness.hashCode()
        return result
    }

    override fun toString(): String = openness.formatIntervalString(min, max)

    companion object {
        val EMPTY = FloatInterval(1f, 0f)
    }
}

class DoubleInterval(
    override val min: Double,
    override val max: Double,
    override val openness: Openness = DEFAULT_OPENNESS
) : Interval<Double> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DoubleInterval) return false
        if (isEmpty() && other.isEmpty()) return true
        return min == other.min && max == other.max && openness == other.openness
    }

    override fun hashCode(): Int {
        if (isEmpty()) return -1
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        result = 31 * result + openness.hashCode()
        return result
    }

    override fun toString(): String = openness.formatIntervalString(min, max)

    companion object {
        val EMPTY = DoubleInterval(1.0, 0.0)
    }
}

private class IntervalImpl<T : Comparable<T>>(
    override val min: T,
    override val max: T,
    override val openness: Openness
) : Interval<T> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Interval<*>) return false
        if (min.javaClass != other.min.javaClass) return false
        if (isEmpty() && other.isEmpty()) return true
        return min == other.min && max == other.max && openness == other.openness
    }

    override fun hashCode(): Int {
        var result = min.hashCode()
        result = 31 * result + max.hashCode()
        result = 31 * result + openness.hashCode()
        return result
    }

    override fun toString(): String = openness.formatIntervalString(min, max)
}

// endregion

fun Interval<*>.isNotEmpty() = !isEmpty()

val Interval<Char>.first get() = if (openness.isLeftOpen) min + 1 else min
val Interval<Char>.last get() = if (openness.isRightOpen) max - 1 else max

val Interval<Int>.first get() = if (openness.isLeftOpen) min + 1 else min
val Interval<Int>.last get() = if (openness.isRightOpen) max - 1 else max

val Interval<Long>.first get() = if (openness.isLeftOpen) min + 1 else min
val Interval<Long>.last get() = if (openness.isRightOpen) max - 1 else max

// Widths:

//val Interval<Char>.width: Int get() = if (isEmpty()) 0 else last - first + 1 // TODO why does this fail to compile?
val Interval<Int>.width: Int get() = if (isEmpty()) 0 else last - first + 1
val Interval<Long>.width: Long get() = if (isEmpty()) 0L else last - first + 1

val Interval<Float>.width: Float get() = if (isEmpty()) 0f else max - min
val Interval<Double>.width: Double get() = if (isEmpty()) 0.0 else max - min

// region Converters between intervals and ranges/progressions

// CharRange:

fun CharRange.toInterval(openness: Openness = DEFAULT_OPENNESS) = when (openness) {
    Openness.OPEN_OPEN -> Interval(start - 1, endInclusive + 1, openness)
    Openness.OPEN_CLOSED -> Interval(start - 1, endInclusive, openness)
    Openness.CLOSED_OPEN -> Interval(start, endInclusive + 1, openness)
    Openness.CLOSED_CLOSED -> Interval(start, endInclusive, openness)
}

fun Interval<Char>.toRange() = CharRange(first, last)
fun Interval<Char>.toProgression(step: Int = 1) = CharProgression.fromClosedRange(first, last, step)

// IntRange:

fun IntRange.toInterval(openness: Openness = DEFAULT_OPENNESS) = when (openness) {
    Openness.OPEN_OPEN -> Interval(start - 1, endInclusive + 1, openness)
    Openness.OPEN_CLOSED -> Interval(start - 1, endInclusive, openness)
    Openness.CLOSED_OPEN -> Interval(start, endInclusive + 1, openness)
    Openness.CLOSED_CLOSED -> Interval(start, endInclusive, openness)
}

fun Interval<Int>.toRange() = IntRange(first, last)
fun Interval<Int>.toProgression(step: Int = 1) = IntProgression.fromClosedRange(first, last, step)

// LongRange:

fun LongRange.toInterval(openness: Openness = DEFAULT_OPENNESS) = when (openness) {
    Openness.OPEN_OPEN -> Interval(start - 1, endInclusive + 1, openness)
    Openness.OPEN_CLOSED -> Interval(start - 1, endInclusive, openness)
    Openness.CLOSED_OPEN -> Interval(start, endInclusive + 1, openness)
    Openness.CLOSED_CLOSED -> Interval(start, endInclusive, openness)
}

fun Interval<Long>.toRange() = LongRange(first, last)
fun Interval<Long>.toProgression(step: Long = 1) = LongProgression.fromClosedRange(first, last, step)

// endregion

enum class Openness(val isLeftOpen: Boolean, val isRightOpen: Boolean) {
    OPEN_OPEN(true, true),
    OPEN_CLOSED(true, false),
    CLOSED_OPEN(false, true),
    CLOSED_CLOSED(false, false);
}

private fun Openness.formatIntervalString(a: Any?, b: Any?): String {
    return buildString {
        append(if (isLeftOpen) "(" else "[")
        append("$a, $b")
        append(if (isRightOpen) ")" else "]")
    }
}

private val DEFAULT_OPENNESS = Openness.CLOSED_OPEN
