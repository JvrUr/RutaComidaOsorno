package com.example.osornogourmet.data.repository

import com.example.osornogourmet.data.local.TokenManager
import com.example.osornogourmet.data.remote.GourmetApi
import com.example.osornogourmet.data.remote.dto.UserLoginRequest
import com.example.osornogourmet.data.remote.dto.UserRegisterRequest
import com.example.osornogourmet.domain.model.User
import com.example.osornogourmet.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteUserRepositoryImpl(
    private val api: GourmetApi,
    private val tokenManager: TokenManager
) : UserRepository {

    override suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        try {
            val response = api.login(UserLoginRequest(email, password))
            tokenManager.saveToken(response.token)
            // Return dummy user since API only returns token
            return@withContext User(id = 1, email = email, name = "User", passwordHash = "")
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    override suspend fun register(user: User): Long = withContext(Dispatchers.IO) {
        val response = api.register(UserRegisterRequest(user.email, user.name, user.passwordHash))
        tokenManager.saveToken(response.token)
        return@withContext 1L
    }

    override suspend fun getUserById(id: Long): User? {
        // Dummy user
        return User(id = id, email = "dummy@example.com", name = "Dummy User", passwordHash = "")
    }
}
