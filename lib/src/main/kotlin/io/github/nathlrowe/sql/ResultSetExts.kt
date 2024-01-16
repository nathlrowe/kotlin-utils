package io.github.nathlrowe.sql

import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

fun <R> ResultSet.map(transform: (ResultSet) -> R): List<R> = buildList {
    while (next()) {
        add(transform(this@map))
    }
}

fun <R : Any> ResultSet.mapNotNull(transform: (ResultSet) -> R?) = buildList {
    while (next()) {
        transform(this@mapNotNull)?.let { add(it) }
    }
}

// region Nullable getters for primitives

fun ResultSet.getBooleanOrNull(columnIndex: Int): Boolean? {
    return getBoolean(columnIndex).let { if (wasNull()) null else it }
}

fun ResultSet.getBooleanOrNull(columnLabel: String): Boolean? {
    return getBoolean(columnLabel).let { if (wasNull()) null else it }
}

fun ResultSet.getByteOrNull(columnIndex: Int): Byte? {
    return getByte(columnIndex).let { if (wasNull()) null else it }
}

fun ResultSet.getByteOrNull(columnLabel: String): Byte? {
    return getByte(columnLabel).let { if (wasNull()) null else it }
}

fun ResultSet.getIntOrNull(columnIndex: Int): Int? {
    return getInt(columnIndex).let { if (wasNull()) null else it }
}

fun ResultSet.getIntOrNull(columnLabel: String): Int? {
    return getInt(columnLabel).let { if (wasNull()) null else it }
}

fun ResultSet.getLongOrNull(columnIndex: Int): Long? {
    return getLong(columnIndex).let { if (wasNull()) null else it }
}

fun ResultSet.getLongOrNull(columnLabel: String): Long? {
    return getLong(columnLabel).let { if (wasNull()) null else it }
}

fun ResultSet.getFloatOrNull(columnIndex: Int): Float? {
    return getFloat(columnIndex).let { if (wasNull()) null else it }
}

fun ResultSet.getFloatOrNull(columnLabel: String): Float? {
    return getFloat(columnLabel).let { if (wasNull()) null else it }
}

fun ResultSet.getDoubleOrNull(columnIndex: Int): Double? {
    return getDouble(columnIndex).let { if (wasNull()) null else it }
}

fun ResultSet.getDoubleOrNull(columnLabel: String): Double? {
    return getDouble(columnLabel).let { if (wasNull()) null else it }
}

// endregion

// region Date and time getters

fun ResultSet.getLocalDate(columnIndex: Int, calendar: Calendar? = null): LocalDate? {
    val date = calendar?.let { getDate(columnIndex, it) } ?: getDate(columnIndex)
    return date?.toLocalDate()
}

fun ResultSet.getLocalDate(columnLabel: String, calendar: Calendar? = null): LocalDate? {
    val date = calendar?.let { getDate(columnLabel, it) } ?: getDate(columnLabel)
    return date?.toLocalDate()
}

fun ResultSet.getLocalTime(columnIndex: Int, calendar: Calendar? = null): LocalTime? {
    val time = calendar?.let { getTime(columnIndex, it) } ?: getTime(columnIndex)
    return time?.toLocalTime()
}

fun ResultSet.getLocalTime(columnLabel: String, calendar: Calendar? = null): LocalTime? {
    val time = calendar?.let { getTime(columnLabel, it) } ?: getTime(columnLabel)
    return time?.toLocalTime()
}

fun ResultSet.getLocalDateTime(columnIndex: Int, calendar: Calendar? = null): LocalDateTime? {
    val timestamp = calendar?.let { getTimestamp(columnIndex, it) } ?: getTimestamp(columnIndex)
    return timestamp?.toLocalDateTime()
}

fun ResultSet.getLocalDateTime(columnLabel: String, calendar: Calendar? = null): LocalDateTime? {
    val timestamp = calendar?.let { getTimestamp(columnLabel, it) } ?: getTimestamp(columnLabel)
    return timestamp?.toLocalDateTime()
}

// endregion
