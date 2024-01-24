package io.github.nathlrowe.sql

enum class InsertMode(val sql: String) {
    INSERT("INSERT"),
    INSERT_IGNORE("INSERT IGNORE"),
    REPLACE("REPLACE");
}