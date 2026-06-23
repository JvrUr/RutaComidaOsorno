package osornogourmet.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import osornogourmet.data.dto.RouteRequest
import osornogourmet.service.RouteService

fun Route.routeRoutes(routeService: RouteService) {
    route("/api/routes") {
        
        // Rutas públicas
        get {
            call.respond(routeService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
            call.respond(routeService.getById(id))
        }

        // Rutas protegidas
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asLong() ?: 0L
                val request = call.receive<RouteRequest>()
                call.respond(routeService.create(userId, request))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
                val request = call.receive<RouteRequest>()
                call.respond(routeService.update(id, request))
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
                routeService.delete(id)
                call.respond(mapOf("message" to "Deleted successfully"))
            }
        }
    }
}
