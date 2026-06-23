package com.example.osornogourmet.domain.repository

import com.example.osornogourmet.domain.model.User

/**
 * Interfaz del repositorio de usuarios.
 *
 * Define las operaciones de acceso a datos relacionadas con la
 * autenticación y gestión de usuarios. La implementación concreta
 * se encuentra en la capa de datos.
 */
interface UserRepository {

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña en texto plano (se comparará con el hash almacenado).
     * @return El [User] autenticado, o null si las credenciales son inválidas.
     */
    suspend fun login(email: String, password: String): User?

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user Datos del usuario a registrar.
     * @return El identificador único asignado al nuevo usuario.
     */
    suspend fun register(user: User): Long

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param id Identificador único del usuario.
     * @return El [User] encontrado, o null si no existe.
     */
    suspend fun getUserById(id: Long): User?
}
