package com.example.osornogourmet.domain.model

/**
 * Modelo de dominio que representa un establecimiento de comida.
 *
 * @property id Identificador único del establecimiento.
 * @property name Nombre del establecimiento.
 * @property description Descripción detallada del establecimiento.
 * @property category Categoría del tipo de comida que ofrece.
 * @property address Dirección física del establecimiento.
 * @property latitude Latitud de la ubicación geográfica.
 * @property longitude Longitud de la ubicación geográfica.
 * @property rating Calificación promedio del establecimiento (0 a 5).
 * @property imageUrl URL de la imagen representativa del establecimiento.
 * @property createdByUserId Identificador del usuario que registró el establecimiento.
 */
data class FoodPlace(
    val id: Long = 0,
    val name: String,
    val description: String,
    val category: FoodCategory,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val createdByUserId: Long = 0
)
