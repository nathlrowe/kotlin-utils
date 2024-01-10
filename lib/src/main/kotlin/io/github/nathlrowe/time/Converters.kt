package io.github.nathlrowe.time

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import java.math.RoundingMode
import java.time.ZoneId
import kotlin.time.toDuration

// region java.time converters for Duration

fun Duration.toJavaDuration(): java.time.Duration = java.time.Duration.ofNanos(inWholeNanoseconds)

fun Duration.Companion.fromJavaDuration(javaDuration: java.time.Duration): Duration {
    return javaDuration.seconds.seconds + javaDuration.nano.nanoseconds
}

fun java.time.Duration.toGhostDuration() = Duration.fromJavaDuration(this)

// endregion

// region kotlinx.datetime converters for Duration

fun Duration.toKotlinDuration(): kotlin.time.Duration = inNanoseconds.toDuration(kotlin.time.DurationUnit.NANOSECONDS)

fun Duration.Companion.fromKotlinDuration(kotlinDuration: kotlin.time.Duration): Duration {
    return kotlinDuration.toComponents { seconds, nanoseconds -> seconds.seconds + nanoseconds.nanoseconds }
}

fun kotlin.time.Duration.toGhostDuration() = Duration.fromKotlinDuration(this)

// endregion

// region java.time converters for Instant

fun Instant.toJavaInstant(): java.time.Instant {
    val epochSeconds = fromEpoch.toLong(SECONDS, RoundingMode.FLOOR)
    val nanoAdjustment = (fromEpoch - epochSeconds.seconds).toLong(NANOSECONDS)
    return java.time.Instant.ofEpochSecond(epochSeconds, nanoAdjustment)
}

fun Instant.Companion.fromJavaInstant(javaInstant: java.time.Instant): Instant {
    return Instant(javaInstant.epochSecond.seconds + javaInstant.nano.nanoseconds)
}

fun java.time.Instant.toGhostInstant() = Instant.fromJavaInstant(this)

fun Instant.toJavaLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): java.time.LocalDateTime {
    return java.time.LocalDateTime.ofInstant(toJavaInstant(), zoneId)
}

fun Instant.Companion.fromJavaLocalDateTime(
    javaLocalDateTime: java.time.LocalDateTime,
    zoneId: ZoneId = ZoneId.systemDefault()
): Instant {
    return javaLocalDateTime.atZone(zoneId).toInstant().toGhostInstant()
}

fun java.time.LocalDateTime.toGhostInstant(zoneId: ZoneId = ZoneId.systemDefault()) =
    Instant.fromJavaLocalDateTime(this, zoneId)

// endregion

// region kotlinx.datetime converters for Instant

fun Instant.toKotlinInstant(): kotlinx.datetime.Instant = toJavaInstant().toKotlinInstant()

fun Instant.Companion.fromKotlinInstant(kotlinInstant: kotlinx.datetime.Instant): Instant {
    return Instant(kotlinInstant.epochSeconds.seconds + kotlinInstant.nanosecondsOfSecond.nanoseconds)
}

fun kotlinx.datetime.Instant.toGhostInstant() = Instant.fromKotlinInstant(this)

fun Instant.toKotlinLocalDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): kotlinx.datetime.LocalDateTime {
    return toKotlinInstant().toLocalDateTime(timeZone)
}

fun Instant.Companion.fromKotlinLocalDateTime(
    kotlinLocalDateTime: kotlinx.datetime.LocalDateTime,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Instant {
    return kotlinLocalDateTime.toInstant(timeZone).toGhostInstant()
}

fun kotlinx.datetime.LocalDateTime.toGhostInstant(timeZone: TimeZone = TimeZone.currentSystemDefault()) =
    Instant.fromKotlinLocalDateTime(this, timeZone)

// endregion
