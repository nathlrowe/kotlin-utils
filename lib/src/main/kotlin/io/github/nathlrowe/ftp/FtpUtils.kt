package io.github.nathlrowe.ftp

import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import org.apache.logging.log4j.LogManager
import java.io.IOException

/**
 * Executes the given [block] with an open [FTPClient] instance configured by [config].
 */
fun <R> withFTPClient(config: FTPConfig, block: FTPClient.() -> R) {
    val logger = LogManager.getLogger()

    // Create and configure FTP client
    val ftp = FTPClient().apply {
        configure(config.getFTPClientConfig())
    }

    try {
        // Connect to FTP client
        if (config.port != null) {
            ftp.connect(config.host, config.port)
        } else {
            ftp.connect(config.host)
        }

        // Check reply
        if (!FTPReply.isPositiveCompletion(ftp.replyCode)) {
            logger.error("FTP server refused connection.")
            ftp.disconnect()
            return // TODO is this the best course of action in this case?
        }

        // Login if given credentials
        if (config.username != null && config.password != null) {
            ftp.login(config.username, config.password)
        }

        // TODO I don't fully know what this does, but I'm pretty sure it's necessary
        ftp.enterLocalPassiveMode()

        // Run block
        block.invoke(ftp)

        // Do logout
        ftp.logout()
    } catch (e: IOException) {
        logger.error(e.stackTraceToString())
    } finally {
        if (ftp.isConnected) runCatching { ftp.disconnect() }
    }
}
