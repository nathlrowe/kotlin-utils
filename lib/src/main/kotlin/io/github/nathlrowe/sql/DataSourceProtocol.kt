package io.github.nathlrowe.sql

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enum of currently-supported data source protocols.
 *
 * @property scheme The scheme to use when creating the database connection URL.
 * @property driverName The name of the database driver class.
 */
@Serializable
enum class DataSourceProtocol(
    val scheme: String,
    val driverName: String,
) {
    @SerialName("mysql") MY_SQL(
        "jdbc:mysql",
        "com.mysql.cj.jdbc.Driver"
    ),

//    @SerialName("mysql-load-balance") MY_SQL_LOAD_BALANCE(
//        "jdbc:mysql:loadbalance",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("mysql-replication") MY_SQL_REPLICATION(
//        "jdbc:mysql:replication",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("mysqlx") MY_SQLX(
//        "mysqlx",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("mysql+srv") MY_SQL_SRV(
//        "jdbc:mysql+srv",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("mysql-load-balance+srv") MY_SQL_LOAD_BALANCE_SRV(
//        "jdbc:mysql+srv:loadbalance",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("mysql-replication+srv") MY_SQL_REPLICATION_SRV(
//        "jdbc:mysql+srv:replication",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("mysqlx+srv") MY_SQLX_SRV(
//        "mysqlx+srv",
//        "com.mysql.cj.jdbc.Driver"
//    ),
//
//    @SerialName("oracle") ORACLE(
//        "jdbc:oracle:thin",
//        "oracle.jdbc.driver.OracleDriver"
//    ),
//
//    @SerialName("oracle-oci") ORACLE_OCI(
//        "jdbc:oracle:oci",
//        "oracle.jdbc.driver.OracleDriver"
//    ),
//
//    @SerialName("oracle-kprb") ORACLE_KPRB(
//        "jdbc:oracle:kprb",
//        "oracle.jdbc.driver.OracleDriver"
//    ),
//
//    @SerialName("postgresql") POSTGRESQL(
//        "jdbc:postgresql",
//        "org.postgresql.Driver"
//    ),
//
//    @SerialName("sql-server") SQL_SERVER(
//        "jdbc:sqlserver",
//        "com.microsoft.jdbc.sqlserver.SQLServerDriver"
//    );
}
