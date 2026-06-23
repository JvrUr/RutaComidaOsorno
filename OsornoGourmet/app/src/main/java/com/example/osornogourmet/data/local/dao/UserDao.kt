package com.example.osornogourmet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.osornogourmet.data.local.entity.UserEntity

/**
 * DAO (Data Access Object) para operaciones de acceso a datos de usuarios.
 *
 * Proporciona métodos para insertar, consultar y autenticar usuarios
 * en la base de datos local Room.
 */
@Dao
interface UserDao {

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param user Entidad del usuario a insertar.
     * @return El ID auto-generado del usuario insertado.
     */
    @Insert
    suspend fun insert(user: UserEntity): Long

    /**
     * Busca un usuario por email y hash de contraseña para autenticación.
     *
     * @param email Correo electrónico del usuario.
     * @param passwordHash Hash SHA-256 de la contraseña.
     * @return El [UserEntity] encontrado, o null si las credenciales son inválidas.
     */
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash LIMIT 1")
    suspend fun login(email: String, passwordHash: String): UserEntity?

    /**
     * Obtiene un usuario por su identificador único.
     *
     * @param id Identificador del usuario.
     * @return El [UserEntity] encontrado, o null si no existe.
     */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): UserEntity?

    /**
     * Busca un usuario por su correo electrónico.
     * Útil para verificar si un email ya está registrado.
     *
     * @param email Correo electrónico a buscar.
     * @return El [UserEntity] encontrado, o null si no existe.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?
}
