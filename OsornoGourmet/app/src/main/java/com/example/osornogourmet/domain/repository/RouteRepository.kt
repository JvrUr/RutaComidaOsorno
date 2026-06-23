package com.example.osornogourmet.domain.repository

import com.example.osornogourmet.domain.model.Route
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de rutas gastronómicas.
 *
 * Define las operaciones CRUD y de consulta para las rutas.
 * La implementación concreta se encuentra en la capa de datos.
 */
interface RouteRepository {

    /**
     * Obtiene todas las rutas gastronómicas como un flujo reactivo.
     *
     * @return [Flow] que emite la lista actualizada de rutas.
     */
    fun getAll(): Flow<List<Route>>

    /**
     * Obtiene las rutas creadas por un usuario específico como un flujo reactivo.
     *
     * @param userId Identificador del usuario creador.
     * @return [Flow] que emite la lista de rutas del usuario.
     */
    fun getByUserId(userId: Long): Flow<List<Route>>

    /**
     * Obtiene una ruta por su identificador.
     *
     * @param id Identificador único de la ruta.
     * @return La [Route] encontrada, o null si no existe.
     */
    suspend fun getById(id: Long): Route?

    /**
     * Inserta una nueva ruta gastronómica.
     *
     * @param route Datos de la ruta a insertar.
     * @return El identificador único asignado a la nueva ruta.
     */
    suspend fun insert(route: Route): Long

    /**
     * Actualiza una ruta gastronómica existente.
     *
     * @param route Datos actualizados de la ruta.
     */
    suspend fun update(route: Route)

    /**
     * Elimina una ruta gastronómica.
     *
     * @param route La ruta a eliminar.
     */
    suspend fun delete(route: Route)
}
