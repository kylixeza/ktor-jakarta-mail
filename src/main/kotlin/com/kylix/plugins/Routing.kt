package com.kylix.plugins

import com.kylix.model.EmailRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/send-email") {
            val emailRequest = call.receive<EmailRequest>()

            try {
                sendNoReplyEmail(emailRequest)
                call.respond(HttpStatusCode.OK, "Email sent successfully")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "Failed to send email")
            }
        }
    }
}

suspend fun sendNoReplyEmail(emailRequest: EmailRequest) = withContext(Dispatchers.IO) {
    val properties = Properties()
    properties["mail.smtp.auth"] = "true"
    properties["mail.smtp.starttls.enable"] = "true"
    properties["mail.smtp.host"] = "smtp.gmail.com" // Replace with your SMTP host
    properties["mail.smtp.port"] = "587"

    val email = System.getenv("EMAIL_ADDRESS")
    val password = System.getenv("EMAIL_PASSWORD")
    val personal = System.getenv("EMAIL_PERSONAL")

    val session = Session.getInstance(properties, object : jakarta.mail.Authenticator() {
        override fun getPasswordAuthentication(): jakarta.mail.PasswordAuthentication {
            return jakarta.mail.PasswordAuthentication(email, password)
        }
    })

    val message = MimeMessage(session)
    message.setFrom(InternetAddress(email, personal)) // Replace with your email
    message.addRecipient(Message.RecipientType.TO, InternetAddress(emailRequest.to))
    message.subject = emailRequest.subject
    message.setText(emailRequest.body)

    Transport.send(message)
}
