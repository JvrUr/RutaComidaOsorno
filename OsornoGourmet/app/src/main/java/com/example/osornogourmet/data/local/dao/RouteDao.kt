package com.example.osornogourmet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.osornogourmet.data.local.entity.RouteEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones de acceso a datos de rutas gastronómicas.
 *
 * Proporciona métodos CRUD completos y consultas reactivas usando [Flow]
 * para mantener la UI actualizada automáticamente ante cambios en las rutas.
 */
@Dao
interface RouteDao {

    /**
     * Obtiene todas las rutas gastronómicas ordenadas por nombre como un flujo reactivo.
     *
     * @return [Flow] que emite la lista actualizada de rutas.
     */
    @Query("SELECT * FROM routes ORDER BY name")
    fun getAll(): Flow<List<RouteEntity>>

    /**
     * Obtiene las rutas creadas por un usuario específico, ordenadas por nombre.
     *
     * @param userId Identificador del usuario creador.
     * @return [Flow] que emite la lista de rutas del usuario.
     */
    @Query("SELECT * FROM routes WHERE createdByUserId = :userId ORDER BY name")
    fun getByUserId(userId: Long): Flow<List<RouteEntity>>

    /**
     * Obtiene una ruta por su identificador único.
     *
     * @param id Identificador de la ruta.
     * @return El [RouteEntity] encontrado, o null si no existe.
     */
    @Query("SELECT * FROM routes WHERE id = :id")
    suspend fun getById(id: Long): RouteEntity?

    /**
     * Inserta una nueva ruta gastronómica.
     *
     * @param route Entidad de la ruta a insertar.
     * @return El ID auto-generado de la ruta insertada.
     */
    @Insert
    suspend fun insert(route: RouteEntity): Long

    /**
     * Actualiza una ruta gastronómica existente.
     *
     * @param route Entidad con los datos actualizados.
     */
    @Update
    suspend fun update(route: RouteEntity)

    /**
     * Elimina una ruta gastronómica.
     *
     * @param route Entidad de la ruta a eliminar.
     */
    @Delete
    suspend fun delete(route: RouteEntity)
}
