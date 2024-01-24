package io.github.nathlrowe.ftp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPClientConfig

/**
 * Serializable [FTPClient] configuration class.
 */
@Serializable
data class FTPConfig(
    @SerialName("host") val host: String,
    @SerialName("port") val port: Int? = 21,
    @SerialName("username") val username: String? = null,
    @SerialName("password") val password: String? = null,
) {
    fun getFTPClientConfig(): FTPClientConfig {
        return FTPClientConfig()
    }
}