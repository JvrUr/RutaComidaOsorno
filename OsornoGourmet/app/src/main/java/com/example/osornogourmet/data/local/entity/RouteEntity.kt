package com.example.osornogourmet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa una ruta gastronómica en la base de datos local.
 *
 * @property id Identificador único auto-generado de la ruta.
 * @property name Nombre descriptivo de la ruta.
 * @property description Descripción detallada de la ruta gastronómica.
 * @property foodPlaceIds IDs de los establecimientos incluidos, almacenados como JSON string.
 * @property createdByUserId Identificador del usuario que creó la ruta.
 * @property estimatedDuration Duración estimada del recorrido (ej: "2 horas").
 * @property estimatedDistance Distancia estimada del recorrido (ej: "5 km").
 */
@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val foodPlaceIds: String = "[]",
    val createdByUserId: Long = 0,
    val estimatedDuration: String = "",
    val estimatedDistance: String = ""
)
