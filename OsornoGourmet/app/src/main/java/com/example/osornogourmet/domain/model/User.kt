package com.example.osornogourmet.domain.model

/**
 * Modelo de dominio que representa un usuario de la aplicación.
 *
 * @property id Identificador único del usuario.
 * @property email Correo electrónico del usuario.
 * @property name Nombre completo del usuario.
 * @property passwordHash Hash de la contraseña del usuario.
 */
data class User(
    val id: Long = 0,
    val email: String,
    val name: String,
    val passwordHash: String
)
