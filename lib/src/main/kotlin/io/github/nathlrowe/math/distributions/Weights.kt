package io.github.nathlrowe.math.distributions

import io.github.nathlrowe.random.RandomValue
import io.github.nathlrowe.utils.toPercentString
import kotlin.random.Random

class Weights<T>(private val rawWeights: Map<T, Double>) : RandomValue<T>, Iterable<Pair<T, Double>> {
    val totalWeight: Double = rawWeights.values.sum()

    init {
        require(rawWeights.isNotEmpty()) { "Weights cannot be empty." }
        require(totalWeight > 0) { "Total weight must be positive: $totalWeight" }
    }

    private val cumulative: List<Pair<T, Double>> = buildList {
        var cumulativeWeight = 0.0
        rawWeights.forEach { (value, weight) ->
            require(weight >= 0) { "Weights must be non-negative." }
            add(Pair(value, cumulativeWeight))
            cumulativeWeight += weight
        }
    }

    operator fun get(value: T): Double = rawWeights[value] ?: throw NoSuchElementException()

    fun probability(value: T): Double = get(value) / totalWeight

    fun toMap(): Map<T, Double> = rawWeights

    fun toNormalizedMap(): Map<T, Double> = rawWeights.mapValues { it.value / totalWeight }

    override fun random(random: Random): T {
        if (cumulative.size == 1) return cumulative.first().first
        val r = random.nextDouble(totalWeight)
        val index = cumulative.binarySearchBy(r) { it.second }
        return (if (index >= 0) cumulative[index] else cumulative[-index-2]).first
    }

    override fun iterator(): Iterator<Pair<T, Double>> = rawWeights.asSequence().map { it.toPair() }.iterator()

    override fun toString(): String {
        return "Weights(" +
            rawWeights.toList().joinToString(", ") {
                "${it.first} = ${it.second} (${(it.second / totalWeight).toPercentString(2)})"
            } +
            ")"
    }
}

fun <T> Weights(rawWeights: Map<T, Number>): Weights<T> = Weights(rawWeights.mapValues { it.value.toDouble() })

fun <T> Weights(rawWeights: Iterable<Pair<T, Number>>): Weights<T> = Weights(rawWeights.toMap())

fun <T> weightsOf(vararg rawWeights: Pair<T, Number>): Weights<T> {
    return Weights(rawWeights.associate { Pair(it.first, it.second.toDouble()) })
}

fun weightsOf(vararg rawWeights: Number): Weights<Int> {
    return rawWeights.mapIndexed { i, weight -> Pair(i, weight) }.toWeights()
}

fun <T> buildWeights(builderAction: WeightsBuilder<T>.() -> Unit): Weights<T> {
    return WeightsBuilder<T>().apply(builderAction).build()
}

fun <T> Iterable<T>.mapWeights(weightSelector: (T) -> Number): Weights<T> {
    return Weights(associateWith(weightSelector))
}

fun <T> Map<T, Number>.toWeights(): Weights<T> = Weights(this)

fun <T> Iterable<Pair<T, Number>>.toWeights(): Weights<T> = Weights(toMap())

class WeightsBuilder<T> internal constructor() {
    private val rawWeights: MutableMap<T, Double> = mutableMapOf()
    private val rawProbabilities: MutableMap<T, Double> = mutableMapOf()

    fun set(value: T, weight: Number) {
        val weightDouble = weight.toDouble()
        require(value !in rawWeights && value !in rawProbabilities)
        require(weightDouble >= 0)
        rawWeights[value] = weightDouble
    }

    fun set(weights: Weights<T>, weight: Number) {
        weights.forEach { (value, _) ->
            set(value, weight.toDouble() * weights.probability(value))
        }
    }

    fun setProbability(value: T, probability: Double) {
        require(value !in rawWeights && value !in rawProbabilities)
        require(probability in 0.0..1.0)
        rawProbabilities[value] = probability
    }

    fun setProbability(weights: Weights<T>, probability: Double) {
        weights.forEach { (value, _) ->
            set(value, probability * weights.probability(value))
        }
    }

    fun build(): Weights<T> {
        return when {
            rawWeights.isEmpty() && rawProbabilities.isEmpty() -> throw IllegalStateException("Weights and probabilities cannot both be empty.")
            rawProbabilities.isEmpty() -> Weights(rawWeights)
            rawWeights.isEmpty() -> Weights(rawProbabilities)
            else -> {
                val rawWeights = buildMap {
                    val totalWeight = rawWeights.values.sum()
                    val totalProbability = rawProbabilities.values.sum()
                    rawWeights.forEach { (value, weight) ->
                        set(value, (weight / totalWeight) * (1 - totalProbability))
                    }
                    rawProbabilities.forEach { (value, probability) ->
                        set(value, probability)
                    }
                }
                Weights(rawWeights)
            }
        }
    }
}