package io.github.nathlrowe.math.distributions

import io.github.nathlrowe.math.INV_SQRT_2PI
import io.github.nathlrowe.math.SQRT_2
import org.apache.commons.math3.special.Erf
import org.apache.commons.math3.util.FastMath
import kotlin.math.abs
import kotlin.random.Random

/**
 * Implementation of the log-normal distribution.
 */
class LogNormalDistribution(
    val mu: Double = 0.0,
    val sigma: Double = 1.0
) : AbstractContinuousDistribution() {
    init {
        require(mu.isFinite()) { "Mu (mean) must be finite." }
        require(sigma.isFinite()) { "Sigma (standard deviation) must be finite." }
        require(sigma > 0) { "Sigma (standard deviation) must be positive." }
    }

    override val mean: Double get() = FastMath.exp(mu + 0.5 * sigma * sigma)

    override val median: Double get() = FastMath.exp(mu)

    override val mode: Double get() = FastMath.exp(mu - sigma * sigma)

    override val variance: Double get() {
        val s2 = sigma * sigma
        return FastMath.expm1(s2) * FastMath.exp(2 * mu + s2)
    }

    override val standardDeviation: Double get() = FastMath.sqrt(variance)

    override fun density(x: Double): Double = pdf(x, mu, sigma)

    override fun cumulativeProbability(x: Double): Double = cdf(x, mu, sigma)

    override fun inverseCumulativeProbability(p: Double): Double {
        TODO("Not yet implemented")
    }

    override fun random(random: Random): Double = FastMath.exp(random.nextNormal(mu, sigma))

    companion object {
        fun pdf(
            x: Double,
            mu: Double = 0.0,
            sigma: Double = 1.0
        ): Double {
            if (x <= 0.0) return 0.0
            val x0 = FastMath.log(x) - mu
            val x1 = -x0 / sigma
            return FastMath.exp(-0.5 * x1 * x1) * INV_SQRT_2PI / (sigma * x)
        }

        fun cdf(
            x: Double,
            mu: Double = 0.0,
            sigma: Double = 1.0
        ): Double {
            if (x <= 0.0) return 0.0
            val x0 = (FastMath.log(x) - mu) / sigma
            if (abs(x0) > 40) return if (x0 < 0.0) 0.0 else 1.0
            return 0.5 + 0.5 * Erf.erf(x0 / SQRT_2)
        }
    }
}

fun Random.nextLogNormal(mu: Double = 0.0, sigma: Double = 1.0): Double = LogNormalDistribution(mu, sigma).random(this)
