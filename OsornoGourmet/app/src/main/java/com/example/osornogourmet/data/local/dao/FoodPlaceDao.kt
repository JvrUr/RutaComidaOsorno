package com.example.osornogourmet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.osornogourmet.data.local.entity.FoodPlaceEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operaciones de acceso a datos de establecimientos de comida.
 *
 * Proporciona métodos CRUD completos y consultas reactivas usando [Flow]
 * para mantener la UI actualizada automáticamente ante cambios en la base de datos.
 */
@Dao
interface FoodPlaceDao {

    /**
     * Obtiene todos los establecimientos ordenados por nombre como un flujo reactivo.
     *
     * @return [Flow] que emite la lista actualizada de establecimientos.
     */
    @Query("SELECT * FROM food_places ORDER BY name")
    fun getAll(): Flow<List<FoodPlaceEntity>>

    /**
     * Obtiene los establecimientos filtrados por categoría, ordenados por nombre.
     *
     * @param category Nombre de la categoría (debe coincidir con el nombre del enum [FoodCategory]).
     * @return [Flow] que emite la lista filtrada de establecimientos.
     */
    @Query("SELECT * FROM food_places WHERE category = :category ORDER BY name")
    fun getByCategory(category: String): Flow<List<FoodPlaceEntity>>

    /**
     * Obtiene un establecimiento por su identificador único.
     *
     * @param id Identificador del establecimiento.
     * @return El [FoodPlaceEntity] encontrado, o null si no existe.
     */
    @Query("SELECT * FROM food_places WHERE id = :id")
    suspend fun getById(id: Long): FoodPlaceEntity?

    /**
     * Inserta un nuevo establecimiento de comida.
     *
     * @param foodPlace Entidad del establecimiento a insertar.
     * @return El ID auto-generado del establecimiento insertado.
     */
    @Insert
    suspend fun insert(foodPlace: FoodPlaceEntity): Long

    /**
     * Actualiza un establecimiento de comida existente.
     *
     * @param foodPlace Entidad con los datos actualizados.
     */
    @Update
    suspend fun update(foodPlace: FoodPlaceEntity)

    /**
     * Elimina un establecimiento de comida.
     *
     * @param foodPlace Entidad del establecimiento a eliminar.
     */
    @Delete
    suspend fun delete(foodPlace: FoodPlaceEntity)

    /**
     * Obtiene una lista de establecimientos por sus identificadores.
     * Útil para cargar los establecimientos asociados a una ruta gastronómica.
     *
     * @param ids Lista de identificadores a buscar.
     * @return Lista de [FoodPlaceEntity] encontrados.
     */
    @Query("SELECT * FROM food_places WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<FoodPlaceEntity>

    /**
     * Cuenta el número total de establecimientos en la base de datos.
     * Útil para verificar si la tabla necesita datos semilla.
     *
     * @return Cantidad total de establecimientos registrados.
     */
    @Query("SELECT COUNT(*) FROM food_places")
    suspend fun count(): Int
}
