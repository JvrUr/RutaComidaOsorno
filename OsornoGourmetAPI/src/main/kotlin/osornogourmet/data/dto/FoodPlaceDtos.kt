package osornogourmet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodPlaceRequest(
    val name: String,
    val description: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String = ""
)

@Serializable
data class FoodPlaceResponse(
    val id: Long,
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
