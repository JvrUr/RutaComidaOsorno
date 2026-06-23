package com.example.osornogourmet.data.remote

import com.example.osornogourmet.data.local.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GourmetApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private var instance: GourmetApi? = null

    fun getInstance(tokenManager: TokenManager): GourmetApi {
        if (instance == null) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val authInterceptor = AuthInterceptor(tokenManager)

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            instance = retrofit.create(GourmetApi::class.java)
        }
        return instance!!
    }
}
