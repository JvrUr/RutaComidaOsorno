package osornogourmet.service

import osornogourmet.config.IllegalArgumentExceptionWithStatus
import osornogourmet.data.dto.LoginRequest
import osornogourmet.data.dto.RegisterRequest
import osornogourmet.data.dto.TokenResponse
import osornogourmet.domain.model.User
import osornogourmet.domain.repository.IUserRepository
import osornogourmet.security.JwtTokenService
import osornogourmet.security.PasswordHasher

class AuthService(
    private val userRepository: IUserRepository,
    private val jwtTokenService: JwtTokenService
) {

    suspend fun register(request: RegisterRequest): TokenResponse {
        if (request.email.isBlank() || request.passwordHash.isBlank() || request.name.isBlank()) {
            throw IllegalArgumentExceptionWithStatus("Email, password and name are required")
        }

        val existingUser = userRepository.findByEmail(request.email)
        if (existingUser != null) {
            throw IllegalArgumentExceptionWithStatus("User already exists")
        }

        val hashedPassword = PasswordHasher.hashPassword(request.passwordHash)
        
        val newUser = User(
            email = request.email,
            name = request.name,
            passwordHash = hashedPassword
        )

        val id = userRepository.insert(newUser)
        val createdUser = newUser.copy(id = id)
        
        val token = jwtTokenService.generateToken(createdUser)

        return TokenResponse(
            token = token,
            userId = id,
            email = createdUser.email,
            name = createdUser.name
        )
    }

    suspend fun login(request: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentExceptionWithStatus("Invalid credentials")

        if (!PasswordHasher.verifyPassword(request.passwordHash, user.passwordHash)) {
            throw IllegalArgumentExceptionWithStatus("Invalid credentials")
        }

        val token = jwtTokenService.generateToken(user)

        return TokenResponse(
            token = token,
            userId = user.id,
            email = user.email,
            name = user.name
        )
    }
}
