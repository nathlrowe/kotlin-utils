package io.github.nathlrowe.sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

fun <R> Connection.useStatement(block: (Statement) -> R): R = createStatement().use(block)

fun <R> Connection.withStatement(block: Statement.() -> R): R = useStatement(block)

fun Connection.prepareStatement(sql: String, autoGenerateKeys: Boolean): PreparedStatement =
    prepareStatement(sql, if (autoGenerateKeys) Statement.RETURN_GENERATED_KEYS else Statement.NO_GENERATED_KEYS)

fun <R> Connection.usePreparedStatement(
    sql: String,
    autoGenerateKeys: Boolean = false,
    block: (PreparedStatement) -> R
): R = prepareStatement(sql, autoGenerateKeys).use(block)

fun <R> Connection.withPreparedStatement(
    sql: String,
    autoGenerateKeys: Boolean = false,
    block: PreparedStatement.() -> R
): R = usePreparedStatement(sql, autoGenerateKeys, block)

fun <R> Connection.executeQuery(sql: String, block: (ResultSet) -> R): R =
    withStatement { block(executeQuery(sql)) }

fun Connection.executeUpdate(sql: String): Int = withStatement { executeUpdate(sql) }

fun <R> Connection.executeUpdate(sql: String, block: (Int) -> R): R =
    withStatement { block(executeUpdate(sql)) }

/**
 * Runs the given [block] as a transaction.
 */
fun <R> Connection.transaction(block: Connection.() -> R): R {
    val prevAutoCommit = autoCommit
    autoCommit = false
    val result = runCatching { block() }
    if (result.isSuccess) commit() else rollback() // TODO consider utilizing savepoint intelligently
    if (prevAutoCommit) autoCommit = true
    return result.getOrThrow()
}

/**
 * Inserts the given values into the table with the given name.
 *
 * This method is intended for use with tables that have an integer ID as their primary key.
 *
 * @param tableName name of the table to insert into
 * @param columnsToValues mapping of column names to the values which should be inserted
 * @param ignore whether to run as `INSERT...` or `INSERT IGNORE...`
 * @return the generated ID
 */
fun Connection.insert(
    tableName: String,
    columnsToValues: Map<Any, Any?>,
    mode: InsertMode = InsertMode.INSERT
): Int {
    val pairs = columnsToValues.toList()
    val columnNames = pairs.joinToString(", ") { it.first.toString() }
    val valuePlaceholders = pairs.joinToString(", ") { "?" }
    val sql = buildString {
        append(mode.sql)
        append(" INTO $tableName ($columnNames) VALUES ($valuePlaceholders)")
    }

    return withPreparedStatement(sql, true) {
        pairs.forEachIndexed { i, (_, value) -> setValue(i+1, value) }

        executeUpdate()

        generatedKeys.use {
            if (it.next()) it.getInt(1) else -1
        }
    }
}

/**
 * Inserts the given [entities] into the table with the given name.
 *
 * This method is intended for use with tables that have an integer ID as their primary key.
 *
 * @param tableName name of the table to insert into
 * @param entities entities to insert
 * @param keys columns in tableName to insert into
 * @param valueSelector selector function mapping entities to the values to be inserted, ordered by [keys]
 * @return list of generated IDs, ordered by [entities]
 */
fun <T> Connection.insert(
    tableName: String,
    entities: Iterable<T>,
    keys: Iterable<Any>,
    valueSelector: (T) -> Iterable<Any?>,
    mode: InsertMode = InsertMode.INSERT
): List<Int> = transaction {
    val columnNames = keys.joinToString(", ") { it.toString() }
    val valuePlaceholders = keys.joinToString(", ") { "?" }
    val sql = buildString {
        append(mode.sql)
        append(" INTO $tableName ($columnNames) VALUES ($valuePlaceholders)")
    }

    // Add entities to statement
    withPreparedStatement(sql, true) {
        entities.forEach { entity ->
            valueSelector(entity).forEachIndexed { i, value ->
                setValue(i+1, value)
            }
            addBatch()
        }

        executeBatch()

        // Return list of generated keys
        generatedKeys.map { it.getInt(1) }
    }
}

/**
 * Inserts the given [entities] into the table with the given name.
 *
 * This method is intended for use with tables that have an integer ID as their primary key.
 *
 * @param tableName name of the table to insert into
 * @param entities entities to insert
 * @param valueSelector map of column names to value selectors, which map entities to the values to be inserted
 * @return list of generated IDs, ordered by [entities]
 */
fun <T> Connection.insert(
    tableName: String,
    entities: Iterable<T>,
    valueSelector: Map<Any, (T) -> Any?>,
    mode: InsertMode = InsertMode.INSERT,
): List<Int> {
    val valueSelectorList = valueSelector.toList()
    return insert(
        tableName = tableName,
        entities = entities,
        keys = valueSelectorList.map { it.first },
        valueSelector = { entity ->
            valueSelectorList.map { it.second(entity) }
        },
        mode = mode
    )
}

/**
 * Updates the given table on the given [where] clause with the given column mappings.
 */
fun Connection.update(
    tableName: String,
    columnsToValues: Map<Any, Any?>,
    where: String
): Int {
    val pairs = columnsToValues.toList()
    val sql = buildString {
        append("UPDATE $tableName SET ")
        append(pairs.joinToString(", ") { "${it.first} = ?" })
        append(" WHERE $where")
    }

    return withPreparedStatement(sql) {
        pairs.forEachIndexed { i, (_, value) -> setValue(i+1, value) }
        executeUpdate()
    }
}

/**
 * Truncates the given table.
 */
fun Connection.truncateTable(tableName: String) {
    executeUpdate("TRUNCATE TABLE $tableName")
}

// TODO count query method:

// count(sql): Long
