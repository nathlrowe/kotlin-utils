package io.github.nathlrowe.sql

import com.mysql.cj.conf.PropertyDefinitions
import io.github.nathlrowe.utils.URI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.apache.commons.dbcp2.*
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.logging.log4j.LogManager
import java.sql.Connection
import java.util.*
import javax.sql.DataSource

/**
 * Serializable [DataSource] configuration class.
 */
@Serializable
data class DataSourceConfig(
    @SerialName("protocol") val protocol: DataSourceProtocol = DataSourceProtocol.MY_SQL,
    @SerialName("host") val host: String? = "localhost",
    @SerialName("port") val port: Int = -1,
    @SerialName("database") val database: String? = null,
    @SerialName("pooling") val pooling: Boolean = true,

    // Authentication properties
    @SerialName("username") val username: String? = null,
    @SerialName("password") val password: String? = null,

    // Security properties
    @SerialName("allow_public_key_retrieval") val allowPublicKeyRetrieval: Boolean = false,
    @SerialName("ssl_mode") val sslMode: PropertyDefinitions.SslMode = PropertyDefinitions.SslMode.PREFERRED,
    @SerialName("use_ssl") val useSsl: Boolean = true,

    // Metadata properties
    @SerialName("no_access_to_procedure_bodies") val noAccessToProcedureBodies: Boolean = false,

    // Datetime types processing properties
    @SerialName("zero_date_time_behavior") val zeroDateTimeBehavior: PropertyDefinitions.ZeroDatetimeBehavior = PropertyDefinitions.ZeroDatetimeBehavior.EXCEPTION,

    // Performance extension properties
    @SerialName("rewrite_batched_statements") val rewriteBatchedStatements: Boolean = false,
) {
    fun getDataSource(): DataSource {
        return if (pooling) getPoolingDataSource() else getBasicDataSource()
    }

    private fun getBasicDataSource(): BasicDataSource {
        val properties = getProperties().apply {
            set(PROP_URL, getJdbcUrl())
        }
        return BasicDataSourceFactory.createDataSource(properties)
    }

    private fun getPoolingDataSource(): PoolingDataSource<PoolableConnection> {
        try {
            // TODO try to have this be specified in properties and see if that breaks things
            Class.forName(protocol.driverName)
        } catch (e: ClassNotFoundException) {
            logger.error("Error loading driver \"${protocol.driverName}\": ${e.stackTraceToString()}")
        }

        val url = getJdbcUrl()
        val properties = getProperties()

        val connectionFactory = DriverManagerConnectionFactory(url, properties)
        val poolableConnectionFactory = PoolableConnectionFactory(connectionFactory, null)
        val connectionPool = GenericObjectPool(poolableConnectionFactory)
        poolableConnectionFactory.pool = connectionPool

        return PoolingDataSource(connectionPool)
    }

    fun getJdbcUrl(): String {
        return when (protocol) {
            DataSourceProtocol.MY_SQL -> {
                URI {
                    scheme = protocol.scheme
                    host = this@DataSourceConfig.host
                    port = this@DataSourceConfig.port
                    database?.let { path = "/$it" }
                }.toString()
            }
            else -> TODO("Not yet implemented")
        }
    }

    // https://dev.mysql.com/doc/connectors/en/connector-j-reference-configuration-properties.html
    fun getProperties(): Properties {
        return Properties().apply {
            // Authentication properties
            if (username != null && password != null) {
                set(PROP_USERNAME, username)
                set(PROP_PASSWORD, password)
            }

            // Security properties
            set(PROP_ALLOW_PUBLIC_KEY_RETRIEVAL, allowPublicKeyRetrieval)
            set(PROP_SSL_MODE, sslMode)
            set(PROP_USE_SSL, useSsl)

            // Metadata properties
            set(PROP_NO_ACCESS_TO_PROCEDURE_BODIES, noAccessToProcedureBodies)

            // Datetime types processing properties
            set(PROP_ZERO_DATE_TIME_BEHAVIOR, zeroDateTimeBehavior)

            // Performance extension properties
            set(PROP_REWRITE_BATCHED_STATEMENTS, rewriteBatchedStatements)
        }
    }

    companion object {
        private val logger = LogManager.getLogger(DataSourceConfig::class.java)

        // TODO add JNDI support
//        const val JNDI_DATA_SOURCE_NAME = "java:comp/env/jdbc/cccproc/cccweb"

        // URL property
        private const val PROP_URL = "url"

        // Authentication properties
        private const val PROP_USERNAME = "user"
        private const val PROP_PASSWORD = "password"

        // Security properties
        private const val PROP_ALLOW_PUBLIC_KEY_RETRIEVAL = "allowPublicKeyRetrieval"
        private const val PROP_SSL_MODE = "sslMode"
        private const val PROP_USE_SSL = "useSsl"

        // Metadata properties
        private const val PROP_NO_ACCESS_TO_PROCEDURE_BODIES = "noAccessToProcedureBodies"

        // Datetime types processing properties
        private const val PROP_ZERO_DATE_TIME_BEHAVIOR = "zeroDateTimeBehavior"

        // Performance extension properties
        private const val PROP_REWRITE_BATCHED_STATEMENTS = "rewriteBatchedStatements"
    }
}

fun DataSourceConfig.getConnection(): Connection = getDataSource().connection

fun <R> DataSourceConfig.useConnection(block: (Connection) -> R) = getDataSource().useConnection(block)
fun <R> DataSourceConfig.withConnection(block: Connection.() -> R) = getDataSource().withConnection(block)
