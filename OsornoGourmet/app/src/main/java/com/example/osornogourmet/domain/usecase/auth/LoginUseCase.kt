package com.example.osornogourmet.domain.usecase.auth

import com.example.osornogourmet.domain.model.User
import com.example.osornogourmet.domain.repository.UserRepository

/**
 * Caso de uso para iniciar sesión.
 *
 * Delega la autenticación al repositorio de usuarios utilizando
 * el correo electrónico y la contraseña proporcionados.
 *
 * @property userRepository Repositorio de usuarios.
 */
class LoginUseCase(
    private val userRepository: UserRepository
) {

    /**
     * Ejecuta el inicio de sesión.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return El [User] autenticado, o null si las credenciales son inválidas.
     */
    suspend operator fun invoke(email: String, password: String): User? {
        return userRepository.login(email, password)
    }
}
