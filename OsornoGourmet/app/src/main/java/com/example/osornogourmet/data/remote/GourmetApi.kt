package com.example.osornogourmet.data.remote

import com.example.osornogourmet.data.remote.dto.*
import retrofit2.http.*

interface GourmetApi {

    // Auth
    @POST("api/auth/login")
    suspend fun login(@Body request: UserLoginRequest): TokenResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: UserRegisterRequest): TokenResponse

    // Food Places
    @GET("api/food-places")
    suspend fun getFoodPlaces(): List<FoodPlaceDto>

    @GET("api/food-places/category/{category}")
    suspend fun getFoodPlacesByCategory(@Path("category") category: String): List<FoodPlaceDto>

    @GET("api/food-places/{id}")
    suspend fun getFoodPlaceById(@Path("id") id: Long): FoodPlaceDto

    @POST("api/food-places")
    suspend fun createFoodPlace(@Body foodPlace: FoodPlaceDto): FoodPlaceDto

    @PUT("api/food-places/{id}")
    suspend fun updateFoodPlace(@Path("id") id: Long, @Body foodPlace: FoodPlaceDto)

    @DELETE("api/food-places/{id}")
    suspend fun deleteFoodPlace(@Path("id") id: Long)

    @POST("api/food-places/batch")
    suspend fun getFoodPlacesBatch(@Body ids: List<Long>): List<FoodPlaceDto>

    // Routes
    @GET("api/routes")
    suspend fun getRoutes(): List<RouteDto>

    @GET("api/routes/user/{userId}")
    suspend fun getRoutesByUserId(@Path("userId") userId: Long): List<RouteDto>

    @GET("api/routes/{id}")
    suspend fun getRouteById(@Path("id") id: Long): RouteDto

    @POST("api/routes")
    suspend fun createRoute(@Body route: RouteDto): RouteDto

    @PUT("api/routes/{id}")
    suspend fun updateRoute(@Path("id") id: Long, @Body route: RouteDto)

    @DELETE("api/routes/{id}")
    suspend fun deleteRoute(@Path("id") id: Long)
}
