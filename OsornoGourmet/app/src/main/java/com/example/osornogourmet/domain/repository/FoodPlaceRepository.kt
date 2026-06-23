package com.example.osornogourmet.domain.repository

import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de establecimientos de comida.
 *
 * Define las operaciones CRUD y de consulta para los establecimientos
 * gastronómicos. La implementación concreta se encuentra en la capa de datos.
 */
interface FoodPlaceRepository {

    /**
     * Obtiene todos los establecimientos de comida como un flujo reactivo.
     *
     * @return [Flow] que emite la lista actualizada de establecimientos.
     */
    fun getAll(): Flow<List<FoodPlace>>

    /**
     * Obtiene los establecimientos filtrados por categoría como un flujo reactivo.
     *
     * @param category Categoría de comida por la cual filtrar.
     * @return [Flow] que emite la lista filtrada de establecimientos.
     */
    fun getByCategory(category: FoodCategory): Flow<List<FoodPlace>>

    /**
     * Obtiene un establecimiento por su identificador.
     *
     * @param id Identificador único del establecimiento.
     * @return El [FoodPlace] encontrado, o null si no existe.
     */
    suspend fun getById(id: Long): FoodPlace?

    /**
     * Inserta un nuevo establecimiento de comida.
     *
     * @param foodPlace Datos del establecimiento a insertar.
     * @return El identificador único asignado al nuevo establecimiento.
     */
    suspend fun insert(foodPlace: FoodPlace): Long

    /**
     * Actualiza un establecimiento de comida existente.
     *
     * @param foodPlace Datos actualizados del establecimiento.
     */
    suspend fun update(foodPlace: FoodPlace)

    /**
     * Elimina un establecimiento de comida.
     *
     * @param foodPlace El establecimiento a eliminar.
     */
    suspend fun delete(foodPlace: FoodPlace)

    /**
     * Obtiene una lista de establecimientos por sus identificadores.
     *
     * Útil para cargar los establecimientos asociados a una ruta gastronómica.
     *
     * @param ids Lista de identificadores de los establecimientos.
     * @return Lista de [FoodPlace] encontrados.
     */
    suspend fun getByIds(ids: List<Long>): List<FoodPlace>
}
