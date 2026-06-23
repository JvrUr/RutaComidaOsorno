package com.example.osornogourmet.data.remote.dto

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserRegisterRequest(
    val email: String,
    val name: String,
    val password: String
)

data class TokenResponse(
    val token: String
)

data class FoodPlaceDto(
    val id: Long? = null,
    val name: String,
    val description: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val imageUrl: String,
    val createdByUserId: Long
)

data class RouteDto(
    val id: Long? = null,
    val name: String,
    val description: String,
    val foodPlaceIds: List<Long>,
    val createdByUserId: Long,
    val estimatedDuration: String,
    val estimatedDistance: String
)
