package io.github.nathlrowe.math

import org.apache.commons.math3.util.FastMath
import kotlin.math.exp
import kotlin.math.pow

/**
 * Implementation of the parametrized Gaussian function.
 *
 * https://en.wikipedia.org/wiki/Gaussian_function
 *
 * @param a Height of the curve's peak.
 * @param b Position of the center of the peak.
 * @param c Width of the peak (equal to standard deviation).
 */
fun gaussian(
    x: Double,
    a: Double = 1.0,
    b: Double = 0.0,
    c: Double = 1.0
): Double {
    val d = (x - b)/c
    return a * FastMath.exp(-d*d/2)
}

/**
 * Implementation of the generalised logistic function.
 *
 * https://en.wikipedia.org/wiki/Generalised_logistic_function
 *
 * @param a The lower (left) asymptote.
 * @param k The upper (right) asymptote when `C = 1.0`.
 * @param b The growth rate.
 * @param v Affects near which asymptote maximum growth occurs. Must be positive.
 * @param q Related to the value of `Y(0.0)`.
 * @param c Typically `1.0`, otherwise the upper asymptote becomes `a + (k - a) / c.pow(1/v)`.
 */
fun logistic(
    t: Double,
    a: Double = 0.0,
    k: Double = 1.0,
    b: Double = 1.0,
    v: Double = 1.0,
    q: Double = 1.0,
    c: Double = 1.0,
): Double {
    require(v > 0) { "v must be positive." }
    return a + (k - a) / ((c + q * FastMath.exp(-b * t)).pow(1 / v))
}
