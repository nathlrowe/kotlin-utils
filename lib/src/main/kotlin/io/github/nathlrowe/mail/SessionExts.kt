package io.github.nathlrowe.mail

import jakarta.activation.DataHandler
import jakarta.activation.FileDataSource
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import org.apache.logging.log4j.LogManager
import java.nio.file.Path
import kotlin.io.path.exists

/**
 * Sends a simple email message.
 *
 * @param from
 * @param recipients
 * @param ccRecipients
 * @param bccRecipients
 * @param subject
 * @param body
 * @param attachments File attachments in the form (file, attachmentFilename).
 */
@Throws(MessagingException::class)
fun Session.sendMessage(
    from: String,
    recipients: Iterable<String>,
    ccRecipients: Iterable<String>? = null,
    bccRecipients: Iterable<String>? = null,
    subject: String? = null,
    body: String? = null,
    attachments: Iterable<Pair<Path, String>>? = null
) {
    val logger = LogManager.getLogger()
    try {
        val recipientsArray = recipients.map { InternetAddress(it) }.toTypedArray()
        val ccRecipientsArray = ccRecipients?.map { InternetAddress(it) }?.toTypedArray()
        val bccRecipientsArray = bccRecipients?.map { InternetAddress(it) }?.toTypedArray()

        // Create message
        val message = MimeMessage(this).apply {
            setFrom(from)
            addRecipients(Message.RecipientType.TO, recipientsArray)
            ccRecipientsArray?.let { addRecipients(Message.RecipientType.CC, it) }
            bccRecipientsArray?.let { addRecipients(Message.RecipientType.BCC, it) }
            this.subject = subject ?: "" // TODO default subject?
        }

        val multipart = MimeMultipart().apply {
            // Add mail body
            val bodyPart = MimeBodyPart().apply {
                setText(body ?: "")
            }
            addBodyPart(bodyPart)

            // Add any file attachments
            attachments?.forEach { (file, filename) ->
                if (file.exists()) {
                    val attachmentPart = MimeBodyPart().apply {
                        val source = FileDataSource(file.toFile())
                        dataHandler = DataHandler(source)
                        fileName = filename
                    }
                    addBodyPart(attachmentPart)
                } else {
                    logger.warn("Attachment file $file does not exist.")
                }
            }
        }

        // Set message content and send
        message.setContent(multipart)
        Transport.send(message)

        logger.debug("Email sent to {} successfully.", recipients.toList())
    } catch (e: MessagingException) {
        logger.error("Error sending email to $recipients: ${e.stackTraceToString()}")
    }
}
