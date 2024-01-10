package io.github.nathlrowe.math.distributions

import io.github.nathlrowe.random.RandomValue

interface Distribution<T> : RandomValue<T>

// region Discrete distribution

interface DiscreteDistribution : Distribution<Long> {
    val mean: Double

    val median: Double

    val mode: Long?

    val standardDeviation: Double

    val variance: Double

    fun probability(x: Long): Double

    fun cumulativeProbability(x: Long): Double

    fun inverseCumulativeProbability(p: Double): Long
}

abstract class AbstractDiscreteDistribution : DiscreteDistribution {
    override val variance: Double get() = standardDeviation * standardDeviation
}

// endregion

// region Continuous distribution

interface ContinuousDistribution : Distribution<Double> {
    val mean: Double

    val median: Double

    val mode: Double?

    val standardDeviation: Double

    val variance: Double

    fun probability(x0: Double, x1: Double): Double

    fun density(x: Double): Double

    fun cumulativeProbability(x: Double): Double

    fun inverseCumulativeProbability(p: Double): Double
}

abstract class AbstractContinuousDistribution : ContinuousDistribution {
    override val variance: Double get() = standardDeviation * standardDeviation

    override fun probability(x0: Double, x1: Double): Double = when {
        x0 == Double.NEGATIVE_INFINITY -> cumulativeProbability(x1)
        x1 == Double.POSITIVE_INFINITY -> 1.0 - cumulativeProbability(x0)
        else -> cumulativeProbability(x1) - cumulativeProbability(x0)
    }
}

// endregion
