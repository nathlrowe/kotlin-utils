package io.github.nathlrowe.math.distributions

import io.github.nathlrowe.math.LN_2
import org.apache.commons.math3.util.CombinatoricsUtils.factorial
import org.apache.commons.math3.util.FastMath
import kotlin.random.Random

/**
 * Implementation of the exponential distribution.
 */
class ExponentialDistribution(
    val lambda: Double
) : AbstractContinuousDistribution() {
    init {
        require(lambda.isFinite()) { "Lambda must be finite." }
        require(lambda > 0) { "Lambda must be positive." }
    }

    override val mean: Double get() = 1 / lambda

    override val median: Double get() = LN_2 / lambda

    override val mode: Double get() = 0.0

    override val standardDeviation: Double get() = 1 / lambda

    override fun density(x: Double): Double {
        return lambda * FastMath.exp(-lambda * x)
    }

    override fun cumulativeProbability(x: Double): Double {
        return if (x <= 0) 0.0 else 1.0 - FastMath.exp(-x / mean)
    }

    override fun inverseCumulativeProbability(p: Double): Double {
        return when {
            p < 0.0 || p > 1.0 -> throw ArithmeticException()
            p == 1.0 -> Double.POSITIVE_INFINITY
            else -> -FastMath.log(1.0 - p) / lambda
        }
    }

    // Source: commons-math3
    override fun random(random: Random): Double {
        var a = 0.0
        var u = random.nextDouble()

        while (u < 0.5) {
            a += EXPONENTIAL_SA_QI[0]
            u *= 2
        }

        u += u - 1

        if (u <= EXPONENTIAL_SA_QI[0]) return lambda * (a + u)

        var i = 0
        var u2 = random.nextDouble()
        var uMin = u2

        do {
            i++
            u2 = random.nextDouble()
            if (u2 < uMin) uMin = u2
        } while (u > EXPONENTIAL_SA_QI[i])

        return lambda * (a + uMin * EXPONENTIAL_SA_QI[0])
    }

    companion object {
        private val EXPONENTIAL_SA_QI = run {
            var qi = 0.0
            var i = 1

            val list = mutableListOf<Double>()
            while (qi < 1) {
                qi += FastMath.pow(LN_2, i) / factorial(i)
                list.add(qi)
                i++
            }

            list.toDoubleArray()
        }
    }
}

fun Random.nextExponential(lambda: Double): Double = ExponentialDistribution(lambda).random(this)
