package io.github.nathlrowe.mail

import jakarta.mail.Authenticator
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.nio.file.Path
import java.util.Properties

/**
 * Serializable SMTP mail configuration class.
 */
@Serializable
data class SmtpConfig(
    @SerialName("host") val host: String,
    @SerialName("port") val port: Int,
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("starttls.enable") val startTlsEnable: Boolean = true
)

/**
 * Returns mail [Session] from this mail configuration.
 */
fun SmtpConfig.getSession(): Session {
    val properties = Properties().apply {
        setProperty("mail.smtp.host", host)
        setProperty("mail.smtp.port", port.toString())
        setProperty("mail.smtp.auth", "true")
        setProperty("mail.smtp.starttls.enable", startTlsEnable.toString())
    }

    return Session.getInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })
}

/**
 * @see Session.sendMessage
 */
@Throws(MessagingException::class)
fun SmtpConfig.sendMessage(
    from: String,
    recipients: Iterable<String>,
    ccRecipients: Iterable<String>? = null,
    bccRecipients: Iterable<String>? = null,
    subject: String? = null,
    body: String? = null,
    attachments: Iterable<Pair<Path, String>>? = null,
) = getSession().sendMessage(
    from = from,
    recipients = recipients,
    ccRecipients = ccRecipients,
    bccRecipients = bccRecipients,
    subject = subject,
    body = body,
    attachments = attachments,
)
