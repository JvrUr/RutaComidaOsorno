package com.example.osornogourmet.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit Singleton para la API de OpenRouteService.
 *
 * Proporciona una instancia única y reutilizable del cliente HTTP
 * configurado con logging de peticiones y el conversor Gson para
 * serialización/deserialización JSON.
 */
object OrsRetrofitClient {

    /** URL base de la API de OpenRouteService */
    private const val BASE_URL = "https://api.openrouteservice.org/"

    /**
     * Interceptor de logging para depuración de peticiones HTTP.
     * Muestra el cuerpo completo de las peticiones y respuestas en logcat.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente OkHttp configurado con el interceptor de logging.
     */
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    /**
     * Instancia de Retrofit configurada con la URL base,
     * conversor Gson y cliente OkHttp personalizado.
     */
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Instancia lazy de la interfaz de la API de OpenRouteService.
     *
     * Se inicializa la primera vez que se accede, creando el proxy
     * Retrofit para realizar las llamadas HTTP.
     */
    val api: OpenRouteServiceApi by lazy {
        retrofit.create(OpenRouteServiceApi::class.java)
    }
}
