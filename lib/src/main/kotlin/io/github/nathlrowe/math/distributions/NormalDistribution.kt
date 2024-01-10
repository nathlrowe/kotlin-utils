package io.github.nathlrowe.math.distributions

import io.github.nathlrowe.math.INV_SQRT_2PI
import io.github.nathlrowe.math.SQRT_2
import org.apache.commons.math3.special.Erf
import org.apache.commons.math3.util.FastMath
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.asJavaRandom

/**
 * Implementation of the normal or Gaussian distribution.
 */
class NormalDistribution(
    val mu: Double = 0.0,
    val sigma: Double = 1.0
) : AbstractContinuousDistribution() {
    init {
        require(mu.isFinite()) { "Mu (mean) must be finite." }
        require(sigma.isFinite()) { "Sigma (standard deviation) must be finite." }
        require(sigma > 0) { "Sigma (standard deviation) must be positive." }
    }

    override val mean: Double get() = mu

    override val median: Double get() = mu

    override val mode: Double get() = mu

    override val standardDeviation: Double get() = sigma

    override val variance: Double get() = sigma * sigma

    override fun density(x: Double): Double = pdf(x, mu, sigma)

    override fun cumulativeProbability(x: Double): Double = cdf(x, mu, sigma)

    override fun inverseCumulativeProbability(p: Double): Double = inverseCdf(p, mu, sigma)

    override fun random(random: Random): Double = random.nextNormal(mu, sigma)

    companion object {
        fun pdf(
            x: Double,
            mu: Double = 0.0,
            sigma: Double = 1.0
        ): Double {
            val x0 = (x - mu) / sigma
            return FastMath.exp(-0.5 * x0 * x0) * INV_SQRT_2PI / sigma
        }

        fun cdf(
            x: Double,
            mu: Double = 0.0,
            sigma: Double = 1.0
        ): Double {
            val x0 = (x - mu) / sigma
            if (abs(x0) > 40) return if (x0 < 0) 0.0 else 1.0
            return 0.5 * Erf.erfc(-x0 / SQRT_2)
        }

        fun inverseCdf(
            p: Double,
            mu: Double = 0.0,
            sigma: Double = 1.0
        ): Double {
            require(p in 0.0 .. 1.0)
            return mu + sigma * SQRT_2 * Erf.erfInv(2*p - 1)
        }
    }
}

fun Random.nextNormal(mu: Double = 0.0, sigma: Double = 0.0): Double = asJavaRandom().nextGaussian() * sigma + mu
