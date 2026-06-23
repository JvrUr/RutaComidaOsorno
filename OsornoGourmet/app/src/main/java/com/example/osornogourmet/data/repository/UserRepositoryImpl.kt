package com.example.osornogourmet.data.repository

import com.example.osornogourmet.data.local.dao.UserDao
import com.example.osornogourmet.data.local.toDomain
import com.example.osornogourmet.data.local.toEntity
import com.example.osornogourmet.domain.model.User
import com.example.osornogourmet.domain.repository.UserRepository
import java.security.MessageDigest

/**
 * Implementación concreta del repositorio de usuarios.
 *
 * Utiliza [UserDao] para acceder a la base de datos local y proporciona
 * la lógica de negocio para autenticación (login/registro) con hash SHA-256.
 *
 * @property userDao DAO de Room para operaciones con usuarios.
 */
class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    /**
     * Inicia sesión verificando las credenciales del usuario.
     *
     * La contraseña se hashea con SHA-256 antes de comparar con el hash
     * almacenado en la base de datos.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña en texto plano.
     * @return El [User] autenticado, o null si las credenciales son inválidas.
     */
    override suspend fun login(email: String, password: String): User? {
        val passwordHash = hashPassword(password)
        return userDao.login(email, passwordHash)?.toDomain()
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Verifica que el correo electrónico no esté ya registrado antes de insertar.
     * La contraseña del usuario se hashea con SHA-256 antes del almacenamiento.
     *
     * @param user Datos del usuario a registrar (passwordHash contiene la contraseña en texto plano).
     * @return El ID asignado al nuevo usuario.
     * @throws IllegalArgumentException Si el correo electrónico ya está registrado.
     */
    override suspend fun register(user: User): Long {
        // Verificar si el email ya está registrado
        val existingUser = userDao.getByEmail(user.email)
        if (existingUser != null) {
            throw IllegalArgumentException("El correo electrónico ya está registrado")
        }

        // Hashear la contraseña antes de almacenarla
        val hashedUser = user.copy(passwordHash = hashPassword(user.passwordHash))
        return userDao.insert(hashedUser.toEntity())
    }

    /**
     * Obtiene un usuario por su identificador único.
     *
     * @param id Identificador del usuario.
     * @return El [User] encontrado, o null si no existe.
     */
    override suspend fun getUserById(id: Long): User? {
        return userDao.getById(id)?.toDomain()
    }

    /**
     * Genera el hash SHA-256 de una contraseña en texto plano.
     *
     * @param password Contraseña en texto plano.
     * @return Hash hexadecimal de la contraseña.
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
