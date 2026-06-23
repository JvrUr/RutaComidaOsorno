package osornogourmet

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import osornogourmet.data.database.FoodPlacesTable
import osornogourmet.data.database.RoutesTable
import osornogourmet.data.database.UsersTable
import osornogourmet.data.dto.FoodPlaceRequest
import osornogourmet.data.dto.RegisterRequest
import osornogourmet.data.repository.FoodPlaceRepository
import osornogourmet.data.repository.RouteRepository
import osornogourmet.data.repository.UserRepository
import osornogourmet.security.JwtTokenService
import osornogourmet.service.AuthService
import osornogourmet.service.FoodPlaceService
import osornogourmet.service.RouteService

object DatabaseSeeder {
    fun seed() {
        // Crear las tablas si no existen
        transaction {
            SchemaUtils.create(UsersTable, FoodPlacesTable, RoutesTable)
        }

        val userRepository = UserRepository()
        val foodPlaceRepository = FoodPlaceRepository()
        
        val authService = AuthService(userRepository, JwtTokenService())
        val foodPlaceService = FoodPlaceService(foodPlaceRepository)

        runBlocking {
            val placesCount = transaction { FoodPlacesTable.selectAll().count() }
            
            if (placesCount == 0L) {
                // Crear usuario admin inicial
                val admin = authService.register(
                    RegisterRequest(
                        email = "admin@osornogourmet.cl",
                        name = "Admin",
                        passwordHash = "admin123" // En una app real, usar contraseñas fuertes
                    )
                )

                // Crear 22 locales iniciales (15 originales + 7 nuevos)
                val lugares = listOf(
                    // --- 15 locales originales ---
                    FoodPlaceRequest("Bavaria", "Comida tradicional", "RESTAURANTE", "O'Higgins 743", -40.5728, -73.1335),
                    FoodPlaceRequest("Café del Centro", "Buen café", "CAFETERIA", "Lord Cochrane 655", -40.5732, -73.1340),
                    FoodPlaceRequest("Club Alemán", "Comida alemana", "RESTAURANTE", "O'Higgins 563", -40.5721, -73.1338),
                    FoodPlaceRequest("Wufehr", "Sandwicheria", "RESTAURANTE", "Eleuterio Ramírez 959", -40.5745, -73.1352),
                    FoodPlaceRequest("Bitte Brot", "Panadería artesanal", "PANADERIA", "Prat 848", -40.5736, -73.1330),
                    FoodPlaceRequest("Pistacho Coffee & Food", "Café de especialidad", "CAFETERIA", "Portal Las Quemas", -40.5680, -73.1185),
                    FoodPlaceRequest("Hotel Waeger Restaurant", "Elegante", "RESTAURANTE", "Av. Juan Mackenna 1040", -40.5753, -73.1360),
                    FoodPlaceRequest("El Bodegón de Osorno", "Parrilla y bar", "RESTAURANTE", "Manuel Antonio Matta 489", -40.5740, -73.1345),
                    FoodPlaceRequest("Dino's Pizza", "Pizza al paso", "COMIDA_RAPIDA", "Manuel Rodríguez 1039", -40.5755, -73.1342),
                    FoodPlaceRequest("Pastelería Rahue", "Tortas caseras", "PASTELERIA", "Av. República 950", -40.5770, -73.1410),
                    FoodPlaceRequest("La Parrilla del Sur", "Carnes de primera", "RESTAURANTE", "Bilbao 876", -40.5760, -73.1325),
                    FoodPlaceRequest("Heladería Colonial", "Helados artesanales", "HELADERIA", "Ramírez 851", -40.5742, -73.1348),
                    FoodPlaceRequest("Marisquería Puerto Osorno", "Pescados y mariscos", "MARISQUERIA", "Freire 530", -40.5718, -73.1350),
                    FoodPlaceRequest("Café Literario", "Libros y café", "CAFETERIA", "Mackenna 680", -40.5747, -73.1355),
                    FoodPlaceRequest("Sandwich Express", "Completos y churrascos", "COMIDA_RAPIDA", "Los Carrera 1120", -40.5763, -73.1315),

                    // --- 7 locales nuevos de Osorno (2026) ---
                    FoodPlaceRequest("Fogón del Sur", "Parrillada y carnes a las brasas", "RESTAURANTE", "Patricio Lynch 1455", -40.5698, -73.1295),
                    FoodPlaceRequest("Café Hausmann", "Café de especialidad y repostería alemana", "CAFETERIA", "Ramírez 898", -40.5738, -73.1342),
                    FoodPlaceRequest("Kumey Sushi", "Sushi y comida japonesa fusión", "RESTAURANTE", "Av. Juan Mackenna 1115", -40.5758, -73.1368),
                    FoodPlaceRequest("Las Brasas de Rahue", "Completos y lomitos artesanales", "COMIDA_RAPIDA", "Av. República 1230", -40.5785, -73.1425),
                    FoodPlaceRequest("Dulce Tentación", "Tortas decoradas y pasteles finos", "PASTELERIA", "Bilbao 920", -40.5762, -73.1318),
                    FoodPlaceRequest("La Caleta Osornina", "Ceviches y empanadas de mariscos", "MARISQUERIA", "Cochrane 590", -40.5725, -73.1345),
                    FoodPlaceRequest("Heladería Suiza", "Helados artesanales y waffles", "HELADERIA", "Freire 785", -40.5730, -73.1320)
                )

                for (lugar in lugares) {
                    foodPlaceService.create(admin.userId, lugar)
                }
                
                println("Base de datos inicializada con 1 usuario y 22 locales.")
            }
        }
    }
}
