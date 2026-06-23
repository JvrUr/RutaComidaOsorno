package osornogourmet.data.repository

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import osornogourmet.data.database.UsersTable
import osornogourmet.data.mapper.UserMapper
import osornogourmet.domain.model.User
import osornogourmet.domain.repository.IUserRepository

class UserRepository : IUserRepository {

    // Helper for transactions
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun findByEmail(email: String): User? = dbQuery {
        UsersTable
            .select { UsersTable.email eq email }
            .map { UserMapper.toDomain(it) }
            .singleOrNull()
    }

    override suspend fun insert(user: User): Long = dbQuery {
        UsersTable.insertAndGetId {
            it[email] = user.email
            it[name] = user.name
            it[passwordHash] = user.passwordHash
        }.value
    }

    override suspend fun findById(id: Long): User? = dbQuery {
        UsersTable
            .select { UsersTable.id eq id }
            .map { UserMapper.toDomain(it) }
            .singleOrNull()
    }
}
