package osornogourmet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val passwordHash: String // Lo dejamos así porque Android puede mandarlo ya hasheado (aunque la buena práctica es texto plano, para que calce con lo de Android lo dejamos así si es que allá hashean)
)

@Serializable
data class RegisterRequest(
    val email: String,
    val name: String,
    val passwordHash: String
)

@Serializable
data class TokenResponse(
    val token: String,
    val userId: Long,
    val email: String,
    val name: String
)

@Serializable
data class TokenRequest(
    val token: String
)
