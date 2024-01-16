package io.github.nathlrowe.sql

abstract class DatabaseSchema

abstract class TableSchema(private val name: String) {
    private val tableColumns: MutableMap<String, TableColumn> = mutableMapOf()

    fun column(name: String): TableColumn {
        require(name !in tableColumns)
        return TableColumn(name).also { tableColumns[name] = it }
    }

    fun getName(): String = name

    override fun toString(): String = name
}

data class TableColumn(val name: String) {
    override fun toString(): String = name
}
