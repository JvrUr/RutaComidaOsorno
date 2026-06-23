package com.example.osornogourmet.domain.usecase.auth

import com.example.osornogourmet.domain.model.User
import com.example.osornogourmet.domain.repository.UserRepository

/**
 * Caso de uso para registrar un nuevo usuario.
 *
 * Incluye validaciones de negocio antes de delegar el registro
 * al repositorio de usuarios.
 *
 * @property userRepository Repositorio de usuarios.
 */
class RegisterUseCase(
    private val userRepository: UserRepository
) {

    /**
     * Ejecuta el registro de un nuevo usuario.
     *
     * Valida que el correo electrónico no esté vacío y que la contraseña
     * tenga al menos 6 caracteres antes de proceder con el registro.
     *
     * @param name Nombre completo del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario (mínimo 6 caracteres).
     * @return [Result] con el identificador del nuevo usuario, o un error de validación.
     */
    suspend operator fun invoke(name: String, email: String, password: String): Result<Long> {
        // Validar que el correo no esté vacío
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo electrónico no puede estar vacío"))
        }

        // Validar que la contraseña tenga al menos 6 caracteres
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 6 caracteres"))
        }

        // Crear el usuario y delegarlo al repositorio
        val user = User(
            email = email,
            name = name,
            passwordHash = password // El hash se realizará en la capa de datos
        )

        return try {
            val userId = userRepository.register(user)
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
