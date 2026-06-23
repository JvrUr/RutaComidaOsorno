package com.example.osornogourmet.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interfaz Retrofit para la API de OpenRouteService.
 *
 * Proporciona acceso al servicio de cálculo de rutas peatonales,
 * utilizado para generar recorridos gastronómicos entre los
 * establecimientos de comida de Osorno.
 *
 * @see OrsDirectionsRequest
 * @see OrsDirectionsResponse
 */
interface OpenRouteServiceApi {

    /**
     * Obtiene las direcciones (ruta) para caminar entre múltiples puntos.
     *
     * IMPORTANTE: Las coordenadas deben estar en formato [longitud, latitud]
     * según la convención de OpenRouteService (inverso a latitud, longitud).
     *
     * @param apiKey Clave de API de OpenRouteService (incluir completa, sin prefijo).
     * @param request Cuerpo de la petición con las coordenadas de los puntos.
     * @return Respuesta con la ruta calculada, incluyendo distancia, duración y geometría.
     */
    @POST("v2/directions/foot-walking/json")
    suspend fun getDirections(
        @Header("Authorization") apiKey: String,
        @Body request: OrsDirectionsRequest
    ): OrsDirectionsResponse
}
