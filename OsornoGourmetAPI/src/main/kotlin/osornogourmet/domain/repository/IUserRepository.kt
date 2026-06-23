package osornogourmet.domain.repository

import osornogourmet.domain.model.User

interface IUserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun insert(user: User): Long
    suspend fun findById(id: Long): User?
}
