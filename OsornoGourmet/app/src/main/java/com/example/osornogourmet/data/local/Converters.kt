package com.example.osornogourmet.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Convertidores de tipos para Room.
 *
 * Room no puede almacenar tipos complejos directamente, por lo que estos
 * convertidores transforman List<Long> a/desde String JSON para su almacenamiento.
 * Se usa principalmente para el campo foodPlaceIds de [RouteEntity].
 */
class Converters {

    /** Instancia de Gson reutilizable para las conversiones */
    private val gson = Gson()

    /**
     * Convierte una lista de IDs (Long) a un String JSON para almacenamiento en Room.
     *
     * @param list Lista de identificadores a convertir.
     * @return Representación JSON de la lista (ej: "[1,2,3]").
     */
    @TypeConverter
    fun fromList(list: List<Long>): String {
        return gson.toJson(list)
    }

    /**
     * Convierte un String JSON a una lista de IDs (Long) al leer desde Room.
     *
     * @param json Cadena JSON que representa la lista (ej: "[1,2,3]").
     * @return Lista de identificadores Long.
     */
    @TypeConverter
    fun toList(json: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(json, type)
    }
}
