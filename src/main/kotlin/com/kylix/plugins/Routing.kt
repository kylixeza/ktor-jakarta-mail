package com.kylix.plugins

import com.kylix.data.sendNoReplyEmail
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
                emailRequest.sendNoReplyEmail()
                call.respond(HttpStatusCode.OK, "Email sent successfully")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, "Failed to send email")
            }
        }
    }
}