package com.vinish.plugins

import com.vinish.model.ErrorResponse
import com.vinish.service.InvalidTaskException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {

        exception<BadRequestException> { call, _ ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Invalid request")
            )
        }

        exception<Throwable> { call, _ ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Internal server error")
            )
        }

        exception<InvalidTaskException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: "Invalid task"))
        }
    }
}
