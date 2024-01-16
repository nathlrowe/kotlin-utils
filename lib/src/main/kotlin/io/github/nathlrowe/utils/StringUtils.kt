package io.github.nathlrowe.utils

import java.text.DecimalFormat

/**
 * Converts the receiving number to a string with the given format configuration.
 *
 * TODO look at DecimalFormat capabilities and add more configuration
 */
fun Number.toString(
    numDecimals: Int = 2,
    useCommas: Boolean = false
): String = getDecimalFormat(numDecimals, useCommas).format(this)

/**
 * Formats this [Float] as a percentage with the given number of decimals.
 */
fun Float.toPercentString(numDecimals: Int = 0): String = "${(this * 100).toString(numDecimals)}%"

/**
 * Formats this [Double] as a percentage with the given number of decimals.
 */
fun Double.toPercentString(numDecimals: Int = 0): String = "${(this * 100).toString(numDecimals)}%"

private fun getDecimalFormat(
    numDecimals: Int = 2,
    useCommas: Boolean = false
): DecimalFormat {
    val pattern = buildString {
        if (useCommas) append("#,##0") else append ("0")
        if (numDecimals > 0) append ("." + "0".repeat(numDecimals))
    }
    return DecimalFormat(pattern)
}
