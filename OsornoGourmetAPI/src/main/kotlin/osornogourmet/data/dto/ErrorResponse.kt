package osornogourmet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)

@Serializable
data class MessageResponse(
    val message: String
)
