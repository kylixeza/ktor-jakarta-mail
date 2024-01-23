package com.kylix.data

import com.kylix.model.EmailRequest
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

suspend fun EmailRequest.sendNoReplyEmail() = withContext(Dispatchers.IO) {
    val properties = Properties()
    properties["mail.smtp.auth"] = "true"
    properties["mail.smtp.starttls.enable"] = "true"
    properties["mail.smtp.host"] = "smtp.gmail.com" // Replace with your SMTP host
    properties["mail.smtp.port"] = "587"

    val email = System.getenv("EMAIL_ADDRESS")
    val password = System.getenv("EMAIL_PASSWORD")
    val personal = System.getenv("EMAIL_PERSONAL")

    val session = Session.getInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(email, password)
        }
    })

    val message = MimeMessage(session)
    message.setFrom(InternetAddress(email, personal)) // Replace with your email
    message.addRecipient(Message.RecipientType.TO, InternetAddress(to))
    message.subject = subject
    message.setText(body)

    Transport.send(message)
}
