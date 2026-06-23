package com.example.osornogourmet.domain.model

/**
 * Enumeración que representa las categorías de establecimientos de comida
 * disponibles en la ciudad de Osorno.
 *
 * @property displayName Nombre legible de la categoría en español.
 */
enum class FoodCategory(val displayName: String) {
    /** Restaurante tradicional */
    RESTAURANTE("Restaurante"),

    /** Cafetería */
    CAFETERIA("Cafetería"),

    /** Panadería */
    PANADERIA("Panadería"),

    /** Establecimiento de comida rápida */
    COMIDA_RAPIDA("Comida Rápida"),

    /** Marisquería */
    MARISQUERIA("Marisquería"),

    /** Pastelería */
    PASTELERIA("Pastelería"),

    /** Heladería */
    HELADERIA("Heladería")
}
