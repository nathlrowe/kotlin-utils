package io.github.nathlrowe.random

import java.util.stream.Stream
import kotlin.random.Random

/**
 * Random value provider interface.
 *
 * @param T The type of values produced by this random value provider.
 */
fun interface RandomValue<out T> {
    /**
     * Returns the next random value using the given [random].
     */
    fun random(random: Random): T
}

fun <T> RandomValue<T>.random(): T = random(Random)

/**
 * Returns a [Iterator] over values produced by this random value provider.
 */
fun <T> RandomValue<T>.iterator(random: Random = Random): Iterator<T> = iterator { random(random) }

/**
 * Returns a [Sequence] of values produced by this random value provider.
 */
fun <T> RandomValue<T>.sequence(random: Random = Random): Sequence<T> = sequence { random(random) }

/**
 * Returns a [Stream] of values produced by this random value provider.
 */
fun <T> RandomValue<T>.stream(random: Random = Random): Stream<T> = Stream.generate { random(random) }

/**
 * Returns a list containing the next [n] random values from this random value provider.
 */
fun <T> RandomValue<T>.sample(n: Int, random: Random = Random): List<T> = sequence(random).take(n).toList()

/**
 * Returns a [RandomValue] that always returns the receiver.
 */
fun <T> T.asRandomValue() = RandomValue { this }

/**
 * Returns a [RandomValue] that produces uniformly random elements from the receiver
 */
fun <T> Collection<T>.toRandomValue() = RandomValue { random(it) }
