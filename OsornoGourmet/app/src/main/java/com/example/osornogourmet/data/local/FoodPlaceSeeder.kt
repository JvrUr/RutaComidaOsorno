package com.example.osornogourmet.data.local

import com.example.osornogourmet.data.local.entity.FoodPlaceEntity

/**
 * Objeto encargado de poblar la base de datos con datos iniciales de
 * establecimientos de comida reales de la ciudad de Osorno, Chile.
 *
 * Se insertan 15 locales gastronómicos representativos de la ciudad,
 * abarcando diversas categorías como restaurantes, cafeterías, panaderías,
 * comida rápida, marisquerías, pastelerías y heladerías.
 *
 * Solo se insertan datos si la tabla está vacía, evitando duplicados
 * en ejecuciones posteriores.
 */
object FoodPlaceSeeder {

    /**
     * Inserta los datos semilla si la tabla de establecimientos está vacía.
     *
     * @param db Instancia de la base de datos [AppDatabase].
     */
    suspend fun seedIfEmpty(db: AppDatabase) {
        val dao = db.foodPlaceDao()

        // Solo insertar si la tabla está vacía para evitar duplicados
        if (dao.count() > 0) return

        val foodPlaces = listOf(
            FoodPlaceEntity(
                name = "Bavaria",
                description = "Restaurante tradicional de Osorno con cocina alemana y chilena. " +
                        "Reconocido por sus platos abundantes y su ambiente acogedor en pleno centro.",
                category = "RESTAURANTE",
                address = "O'Higgins 743, Osorno",
                latitude = -40.5728,
                longitude = -73.1335,
                rating = 4.3f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Café del Centro",
                description = "Cafetería céntrica ideal para un café de la tarde. " +
                        "Ofrece bebidas calientes artesanales, pasteles caseros y sándwiches frescos.",
                category = "CAFETERIA",
                address = "Lord Cochrane 655, Osorno",
                latitude = -40.5732,
                longitude = -73.1340,
                rating = 4.1f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Club Alemán",
                description = "Histórico restaurante con más de 100 años de tradición germana en Osorno. " +
                        "Especialidad en chucrut, codillo y kuchen artesanal.",
                category = "RESTAURANTE",
                address = "O'Higgins 563, Osorno",
                latitude = -40.5721,
                longitude = -73.1338,
                rating = 4.5f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Wufehr",
                description = "Restaurante de comida criolla y alemana con décadas de historia. " +
                        "Famoso por sus preparaciones caseras y sus postres tradicionales del sur de Chile.",
                category = "RESTAURANTE",
                address = "Eleuterio Ramírez 959, Osorno",
                latitude = -40.5745,
                longitude = -73.1352,
                rating = 4.2f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Bitte Brot",
                description = "Panadería artesanal especializada en panes de masa madre y bollería alemana. " +
                        "Ingredientes naturales y recetas tradicionales del sur de Chile.",
                category = "PANADERIA",
                address = "Prat 848, Osorno",
                latitude = -40.5736,
                longitude = -73.1330,
                rating = 4.6f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Pistacho Coffee & Food",
                description = "Cafetería moderna con ambiente relajado. " +
                        "Ofrece café de especialidad, brunch los fines de semana y repostería creativa.",
                category = "CAFETERIA",
                address = "Portal Las Quemas, Osorno",
                latitude = -40.5680,
                longitude = -73.1185,
                rating = 4.4f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Hotel Waeger Restaurant",
                description = "Restaurante del emblemático Hotel Waeger. Cocina internacional y regional " +
                        "con ingredientes locales de la Región de Los Lagos.",
                category = "RESTAURANTE",
                address = "Av. Juan Mackenna 1040, Osorno",
                latitude = -40.5753,
                longitude = -73.1360,
                rating = 4.0f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "El Bodegón de Osorno",
                description = "Restaurante con sabor casero que rescata la cocina chilena tradicional. " +
                        "Cazuelas, empanadas y pastel de choclo como los de la abuela.",
                category = "RESTAURANTE",
                address = "Manuel Antonio Matta 489, Osorno",
                latitude = -40.5740,
                longitude = -73.1345,
                rating = 4.1f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Dino's Pizza",
                description = "Cadena local de comida rápida especializada en pizzas artesanales, " +
                        "hamburguesas y papas fritas. Ideal para una comida rápida y económica.",
                category = "COMIDA_RAPIDA",
                address = "Manuel Rodríguez 1039, Osorno",
                latitude = -40.5755,
                longitude = -73.1342,
                rating = 3.8f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Pastelería Rahue",
                description = "Pastelería tradicional del barrio Rahue. Conocida por sus tortas de " +
                        "milhojas, kuchen de nuez y empanadas dulces artesanales.",
                category = "PASTELERIA",
                address = "Av. República 950, Osorno",
                latitude = -40.5770,
                longitude = -73.1410,
                rating = 4.2f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "La Parrilla del Sur",
                description = "Restaurante parrillero con las mejores carnes a la brasa de Osorno. " +
                        "Cortes premium, ensaladas frescas y vinos de la zona central de Chile.",
                category = "RESTAURANTE",
                address = "Bilbao 876, Osorno",
                latitude = -40.5760,
                longitude = -73.1325,
                rating = 4.3f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Heladería Colonial",
                description = "Heladería artesanal con sabores únicos del sur de Chile. " +
                        "Helados de murta, calafate, y los clásicos de siempre con ingredientes naturales.",
                category = "HELADERIA",
                address = "Ramírez 851, Osorno",
                latitude = -40.5742,
                longitude = -73.1348,
                rating = 4.5f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Marisquería Puerto Osorno",
                description = "El mejor marisco fresco de la zona. Especialidad en curanto en olla, " +
                        "caldillo de congrio, empanadas de mariscos y ceviche sureño.",
                category = "MARISQUERIA",
                address = "Freire 530, Osorno",
                latitude = -40.5718,
                longitude = -73.1350,
                rating = 4.7f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Café Literario",
                description = "Cafetería cultural con rincón de lectura y exposiciones de arte local. " +
                        "Café de grano, infusiones y repostería fina en un ambiente bohemio.",
                category = "CAFETERIA",
                address = "Mackenna 680, Osorno",
                latitude = -40.5747,
                longitude = -73.1355,
                rating = 4.4f,
                imageUrl = "",
                createdByUserId = 0
            ),
            FoodPlaceEntity(
                name = "Sandwich Express",
                description = "Local de comida rápida con sándwiches generosos estilo chileno. " +
                        "Lomitos, churrascos, completos y jugos naturales a precio accesible.",
                category = "COMIDA_RAPIDA",
                address = "Los Carrera 1120, Osorno",
                latitude = -40.5763,
                longitude = -73.1315,
                rating = 3.5f,
                imageUrl = "",
                createdByUserId = 0
            )
        )

        // Insertar todos los establecimientos semilla en la base de datos
        foodPlaces.forEach { dao.insert(it) }
    }
}
