package com.example.osornogourmet.domain.model

/**
 * Modelo de dominio que representa una ruta gastronómica.
 *
 * Una ruta agrupa varios establecimientos de comida en un recorrido
 * sugerido para el usuario.
 *
 * @property id Identificador único de la ruta.
 * @property name Nombre descriptivo de la ruta.
 * @property description Descripción detallada de la ruta gastronómica.
 * @property foodPlaceIds Lista de identificadores de los establecimientos incluidos en la ruta.
 * @property createdByUserId Identificador del usuario que creó la ruta.
 * @property estimatedDuration Duración estimada del recorrido (ej: "2 horas").
 * @property estimatedDistance Distancia estimada del recorrido (ej: "5 km").
 */
data class Route(
    val id: Long = 0,
    val name: String,
    val description: String,
    val foodPlaceIds: List<Long> = emptyList(),
    val createdByUserId: Long = 0,
    val estimatedDuration: String = "",
    val estimatedDistance: String = ""
)
