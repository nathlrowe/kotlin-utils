package io.github.nathlrowe.sql

import java.sql.Date
import java.sql.PreparedStatement
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun PreparedStatement.setValue(parameterIndex: Int, value: Any?) {
    when (value) {
        is Enum<*> -> setString(parameterIndex, value.name)
        is LocalDate -> setDate(parameterIndex, Date.valueOf(value))
        is LocalDateTime -> setTimestamp(parameterIndex, Timestamp.valueOf(value))
        is LocalTime -> setTime(parameterIndex, Time.valueOf(value))
        else -> setObject(parameterIndex, value)
    }
}
