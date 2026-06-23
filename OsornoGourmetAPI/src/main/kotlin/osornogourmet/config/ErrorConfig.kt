package osornogourmet.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import osornogourmet.data.dto.ErrorResponse

class IllegalArgumentExceptionWithStatus(message: String, val statusCode: HttpStatusCode = HttpStatusCode.BadRequest) : Exception(message)

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is IllegalArgumentExceptionWithStatus -> {
                    call.respond(cause.statusCode, ErrorResponse(cause.message ?: "Bad Request"))
                }
                is IllegalArgumentException -> {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: "Bad Request"))
                }
                is IllegalStateException -> {
                    call.respond(HttpStatusCode.Conflict, ErrorResponse(cause.message ?: "Conflict"))
                }
                else -> {
                    cause.printStackTrace()
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Internal Server Error: ${cause.message}"))
                }
            }
        }
    }
}
