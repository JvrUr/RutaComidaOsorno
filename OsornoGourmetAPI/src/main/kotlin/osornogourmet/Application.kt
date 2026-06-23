package osornogourmet

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import osornogourmet.config.configureAuthentication
import osornogourmet.config.configureErrorHandling
import osornogourmet.config.DatabaseConfig
import osornogourmet.data.repository.FoodPlaceRepository
import osornogourmet.data.repository.RouteRepository
import osornogourmet.data.repository.UserRepository
import osornogourmet.security.JwtTokenService
import osornogourmet.service.AuthService
import osornogourmet.service.FoodPlaceService
import osornogourmet.service.RouteService

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 1. Conexión DB
    DatabaseConfig.connect()
    DatabaseSeeder.seed()

    // 2. Dependencias / Inyección manual (similar a AppDatabase)
    val userRepository = UserRepository()
    val foodPlaceRepository = FoodPlaceRepository()
    val routeRepository = RouteRepository()

    val jwtTokenService = JwtTokenService()

    val authService = AuthService(userRepository, jwtTokenService)
    val foodPlaceService = FoodPlaceService(foodPlaceRepository)
    val routeService = RouteService(routeRepository)

    // 3. Plugins
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost() // Solo para desarrollo
    }

    configureErrorHandling()
    configureAuthentication()
    
    // 4. Rutas
    configureRouting(authService, foodPlaceService, routeService)
}
