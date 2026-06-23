package osornogourmet.data.mapper

import org.jetbrains.exposed.sql.ResultRow
import osornogourmet.data.database.UsersTable
import osornogourmet.domain.model.User

object UserMapper {
    fun toDomain(row: ResultRow): User {
        return User(
            id = row[UsersTable.id].value,
            email = row[UsersTable.email],
            name = row[UsersTable.name],
            passwordHash = row[UsersTable.passwordHash]
        )
    }
}
