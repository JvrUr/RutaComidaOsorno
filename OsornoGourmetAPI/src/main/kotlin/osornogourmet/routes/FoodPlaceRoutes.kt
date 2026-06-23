package osornogourmet.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import osornogourmet.data.dto.FoodPlaceRequest
import osornogourmet.service.FoodPlaceService

fun Route.foodPlaceRoutes(foodPlaceService: FoodPlaceService) {
    route("/api/food-places") {
        
        // Rutas públicas (ver locales)
        get {
            call.respond(foodPlaceService.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
            call.respond(foodPlaceService.getById(id))
        }

        get("/category/{cat}") {
            val cat = call.parameters["cat"] ?: throw IllegalArgumentException("Invalid category")
            call.respond(foodPlaceService.getByCategory(cat))
        }

        // Rutas protegidas (crear, actualizar, borrar locales)
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asLong() ?: 0L
                val request = call.receive<FoodPlaceRequest>()
                call.respond(foodPlaceService.create(userId, request))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
                val request = call.receive<FoodPlaceRequest>()
                call.respond(foodPlaceService.update(id, request))
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull() ?: throw IllegalArgumentException("Invalid ID")
                foodPlaceService.delete(id)
                call.respond(mapOf("message" to "Deleted successfully"))
            }
        }
    }
}
