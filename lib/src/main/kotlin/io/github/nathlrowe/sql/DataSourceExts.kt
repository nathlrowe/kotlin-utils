package io.github.nathlrowe.sql

import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

fun <R> DataSource.useConnection(block: (Connection) -> R) = connection.use(block)

fun <R> DataSource.useConnection(
    username: String,
    password: String,
    block: (Connection) -> R
) = getConnection(username, password).use(block)

fun <R> DataSource.withConnection(block: Connection.() -> R) = connection.use(block)

fun <R> DataSource.withConnection(
    username: String,
    password: String,
    block: Connection.() -> R
) = getConnection(username, password).use(block)

fun <R> DataSource.useStatement(block: (Statement) -> R) = withConnection { useStatement(block) }

fun <R> DataSource.withStatement(block: Statement.() -> R) = withConnection { withStatement(block) }

fun <R> DataSource.executeQuery(sql: String, block: (ResultSet) -> R) = withConnection { executeQuery(sql, block) }

fun DataSource.executeUpdate(sql: String) = withConnection { executeUpdate(sql) }

fun <R> DataSource.executeUpdate(sql: String, block: (Int) -> R) = withConnection { executeUpdate(sql, block) }

fun DataSource.truncateTable(tableName: String) = withConnection { truncateTable(tableName) }
