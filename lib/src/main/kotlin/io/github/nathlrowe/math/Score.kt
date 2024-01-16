package io.github.nathlrowe.math

import io.github.nathlrowe.math.distributions.NormalDistribution
import io.github.nathlrowe.math.distributions.nextNormal
import kotlin.random.Random

/**
 * A score is a numeric measure of how an entity compares to other entities.
 */
interface Score {
    val absolute: Double
    val gaussian: Double
}

fun Score(rating: Double): Score = Rating(rating)

@JvmInline
value class Rating(override val absolute: Double) : Score {
    override val gaussian: Double get() = NormalDistribution.inverseCdf(absolute)
}

@JvmInline
value class ZScore(override val gaussian: Double) : Score {
    override val absolute: Double get() = NormalDistribution.cdf(gaussian)
}

fun Random.nextScore() = Score(nextDouble())

fun Random?.nextScoreOrMid() = this?.nextScore() ?: Score(0.5)

fun Random.nextRating() = Rating(nextDouble())

fun Random?.nextRatingOrMid() = this?.nextRating() ?: Rating(0.5)

fun Random.nextZScore(mu: Double = 0.0, sigma: Double = 1.0) = ZScore(nextNormal(mu, sigma))

fun Random?.nextZScoreOrMu(mu: Double = 0.0, sigma: Double = 1.0) = this?.nextZScore(mu, sigma) ?: ZScore(mu)

fun Score.toRating(): Rating = if (this is Rating) this else Rating(absolute)

fun Score.toZScore(): ZScore = if (this is ZScore) this else ZScore(gaussian)
