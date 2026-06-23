package osornogourmet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RouteRequest(
    val name: String,
    val description: String,
    val foodPlaceIds: List<Long>
)

@Serializable
data class RouteResponse(
    val id: Long,
    val name: String,
    val description: String,
    val foodPlaceIds: List<Long>,
    val createdByUserId: Long,
    val estimatedDuration: String,
    val estimatedDistance: String
)
