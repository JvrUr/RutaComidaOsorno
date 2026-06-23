package com.example.osornogourmet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa un establecimiento de comida en la base de datos local.
 *
 * @property id Identificador único auto-generado del establecimiento.
 * @property name Nombre del establecimiento.
 * @property description Descripción detallada del establecimiento.
 * @property category Categoría del establecimiento almacenada como String (nombre del enum).
 * @property address Dirección física del establecimiento.
 * @property latitude Latitud de la ubicación geográfica.
 * @property longitude Longitud de la ubicación geográfica.
 * @property rating Calificación promedio del establecimiento (0.0 a 5.0).
 * @property imageUrl URL de la imagen representativa del establecimiento.
 * @property createdByUserId Identificador del usuario que registró el establecimiento (0 = sistema).
 */
@Entity(tableName = "food_places")
data class FoodPlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val createdByUserId: Long = 0
)
