package osornogourmet

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import osornogourmet.routes.authRoutes
import osornogourmet.routes.foodPlaceRoutes
import osornogourmet.routes.routeRoutes
import osornogourmet.service.AuthService
import osornogourmet.service.FoodPlaceService
import osornogourmet.service.RouteService

fun Application.configureRouting(
    authService: AuthService,
    foodPlaceService: FoodPlaceService,
    routeService: RouteService
) {
    routing {
        get("/") {
            call.respondText("OsornoGourmet API is running! 🚀")
        }

        authRoutes(authService)
        foodPlaceRoutes(foodPlaceService)
        routeRoutes(routeService)
    }
}
