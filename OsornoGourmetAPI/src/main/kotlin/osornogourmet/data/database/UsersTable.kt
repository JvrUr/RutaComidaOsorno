package osornogourmet.data.database

import org.jetbrains.exposed.dao.id.LongIdTable

object UsersTable : LongIdTable("usuarios") {
    val correo = varchar("correo", 255).uniqueIndex()
    val nombre = varchar("nombre", 255)
    val contrasenaHash = varchar("contrasena_hash", 255)
}
