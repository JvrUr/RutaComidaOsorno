package com.example.osornogourmet.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa un usuario en la base de datos local.
 *
 * @property id Identificador único auto-generado del usuario.
 * @property email Correo electrónico del usuario (debe ser único).
 * @property name Nombre completo del usuario.
 * @property passwordHash Hash SHA-256 de la contraseña del usuario.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val name: String,
    val passwordHash: String
)
