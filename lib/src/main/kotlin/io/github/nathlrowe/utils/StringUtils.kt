package io.github.nathlrowe.utils

/**
 * Returns the string value of this [Float], formatted with the specified number of decimals.
 */
fun Float.toString(numDecimals: Int = 3): String {
    require(numDecimals >= 0) { "numDecimals cannot be negative." }
    return String.format("%.${numDecimals}f", this)
}

/**
 * Returns the string value of this [Double], formatted with the specified number of decimals.
 */
fun Double.toString(numDecimals: Int = 3): String {
    require(numDecimals >= 0) { "numDecimals cannot be negative." }
    return String.format("%.${numDecimals}f", this)
}

/**
 * Formats this [Float] as a percentage with the given number of decimals.
 */
fun Float.toPercentString(numDecimals: Int = 0): String = "${(this * 100).toString(numDecimals)}%"

/**
 * Formats this [Double] as a percentage with the given number of decimals.
 */
fun Double.toPercentString(numDecimals: Int = 0): String = "${(this * 100).toString(numDecimals)}%"
