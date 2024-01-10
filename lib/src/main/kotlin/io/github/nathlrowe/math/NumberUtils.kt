package io.github.nathlrowe.math

import java.math.RoundingMode
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Converts this [Float] to an [Int] using the given [RoundingMode].
 */
fun Float.toInt(roundingMode: RoundingMode = RoundingMode.DOWN): Int {
    return when (roundingMode) {
        RoundingMode.UP -> if (this >= 0) ceil(this).toInt() else floor(this).toInt()
        RoundingMode.DOWN -> toInt()
        RoundingMode.CEILING -> ceil(this).toInt()
        RoundingMode.FLOOR -> floor(this).toInt()
        RoundingMode.HALF_UP -> if (this >= 0) (this + 0.5).toInt() else (this - 0.5).toInt()
        RoundingMode.HALF_DOWN -> if (this >= 0) ceil(this - 0.5).toInt() else floor(this + 0.5).toInt()
        RoundingMode.HALF_EVEN -> TODO("Not yet implemented")
        RoundingMode.UNNECESSARY -> toInt().also { if (this != it.toFloat()) throw ArithmeticException() }
    }
}

/**
 * Converts this [Float] to a [Long] using the given [RoundingMode].
 */
fun Float.toLong(roundingMode: RoundingMode = RoundingMode.DOWN): Long {
    return when (roundingMode) {
        RoundingMode.UP -> if (this >= 0) ceil(this).toLong() else floor(this).toLong()
        RoundingMode.DOWN -> toLong()
        RoundingMode.CEILING -> ceil(this).toLong()
        RoundingMode.FLOOR -> floor(this).toLong()
        RoundingMode.HALF_UP -> if (this >= 0) (this + 0.5).toLong() else (this - 0.5).toLong()
        RoundingMode.HALF_DOWN -> if (this >= 0) ceil(this - 0.5).toLong() else floor(this + 0.5).toLong()
        RoundingMode.HALF_EVEN -> TODO("Not yet implemented")
        RoundingMode.UNNECESSARY -> toLong().also { if (this != it.toFloat()) throw ArithmeticException() }
    }
}

/**
 * Converts this [Double] to an [Int] using the given [RoundingMode].
 */
fun Double.toInt(roundingMode: RoundingMode = RoundingMode.DOWN): Int {
    return when (roundingMode) {
        RoundingMode.UP -> if (this >= 0) ceil(this).toInt() else floor(this).toInt()
        RoundingMode.DOWN -> toInt()
        RoundingMode.CEILING -> ceil(this).toInt()
        RoundingMode.FLOOR -> floor(this).toInt()
        RoundingMode.HALF_UP -> if (this >= 0) (this + 0.5).toInt() else (this - 0.5).toInt()
        RoundingMode.HALF_DOWN -> if (this >= 0) ceil(this - 0.5).toInt() else floor(this + 0.5).toInt()
        RoundingMode.HALF_EVEN -> TODO("Not yet implemented")
        RoundingMode.UNNECESSARY -> toInt().also { if (this != it.toDouble()) throw ArithmeticException() }
    }
}

/**
 * Converts this [Double] to a [Long] using the given [RoundingMode].
 */
fun Double.toLong(roundingMode: RoundingMode = RoundingMode.DOWN): Long {
    return when (roundingMode) {
        RoundingMode.UP -> if (this >= 0) ceil(this).toLong() else floor(this).toLong()
        RoundingMode.DOWN -> toLong()
        RoundingMode.CEILING -> ceil(this).toLong()
        RoundingMode.FLOOR -> floor(this).toLong()
        RoundingMode.HALF_UP -> if (this >= 0) (this + 0.5).toLong() else (this - 0.5).toLong()
        RoundingMode.HALF_DOWN -> if (this >= 0) ceil(this - 0.5).toLong() else floor(this + 0.5).toLong()
        RoundingMode.HALF_EVEN -> TODO("Not yet implemented")
        RoundingMode.UNNECESSARY -> toLong().also { if (this != it.toDouble()) throw ArithmeticException() }
    }
}
