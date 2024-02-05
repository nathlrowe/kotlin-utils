package io.github.nathlrowe.math.distributions

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Implementation of the uniform distribution.
 */
class UniformDistribution(
    val min: Double,
    val max: Double
) : AbstractContinuousDistribution() {
    init {
        require(min.isFinite()) { "Min must be finite." }
        require(max.isFinite()) { "Max must be finite." }
        require(max >= min) { "Max cannot be less than min." }
    }

    override val mean: Double get() = (min + max) / 2

    override val median: Double get() = mean

    override val mode: Double? get() = null

    override val variance: Double get() = (max - min).pow(2) / 12

    override val standardDeviation: Double get() = sqrt(variance)

    override fun density(x: Double): Double {
        return when {
            x < min || x > max -> 0.0
            max == min -> Double.POSITIVE_INFINITY
            else -> 1/(max - min)
        }
    }

    override fun cumulativeProbability(x: Double): Double {
        return when {
            x < min -> 0.0
            x > max -> 1.0
            max == min -> 0.5
            else -> (x - min)/(max - min)
        }
    }

    override fun inverseCumulativeProbability(p: Double): Double {
        return when {
            p < 0.0 || p > 1.0 -> throw ArithmeticException()
            p == 0.0 -> min
            p == 1.0 -> max
            max == min -> min
            else -> p*(max - min)
        }
    }

    override fun random(random: Random): Double = random.nextDouble(min, max)

    companion object {
        val STANDARD = UniformDistribution(0.0, 1.0)
    }
}
