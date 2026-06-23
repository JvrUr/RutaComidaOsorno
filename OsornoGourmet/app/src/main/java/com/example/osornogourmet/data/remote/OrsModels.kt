package com.example.osornogourmet.data.remote

/**
 * Modelo de petición para la API de direcciones de OpenRouteService.
 *
 * @property coordinates Lista de coordenadas en formato [longitud, latitud].
 *                        IMPORTANTE: OpenRouteService usa [lng, lat], NO [lat, lng].
 *                        Ejemplo: [[-73.1335, -40.5728], [-73.1340, -40.5732]]
 */
data class OrsDirectionsRequest(
    val coordinates: List<List<Double>>
)

/**
 * Modelo de respuesta de la API de direcciones de OpenRouteService.
 *
 * @property routes Lista de rutas calculadas. Normalmente contiene una sola ruta.
 */
data class OrsDirectionsResponse(
    val routes: List<OrsRoute>
)

/**
 * Modelo de una ruta individual en la respuesta de OpenRouteService.
 *
 * @property summary Resumen con la distancia y duración total de la ruta.
 * @property geometry Polyline codificada con el trazado geográfico de la ruta.
 *                     Se debe decodificar con [PolylineDecoder] para obtener los puntos.
 */
data class OrsRoute(
    val summary: OrsSummary,
    val geometry: String
)

/**
 * Resumen de distancia y duración de una ruta de OpenRouteService.
 *
 * @property distance Distancia total en metros.
 * @property duration Duración estimada en segundos.
 */
data class OrsSummary(
    val distance: Double,
    val duration: Double
)
