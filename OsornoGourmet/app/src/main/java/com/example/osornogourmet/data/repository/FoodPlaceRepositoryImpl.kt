package com.example.osornogourmet.data.repository

import com.example.osornogourmet.data.local.dao.FoodPlaceDao
import com.example.osornogourmet.data.local.toDomain
import com.example.osornogourmet.data.local.toEntity
import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de establecimientos de comida.
 *
 * Utiliza [FoodPlaceDao] para acceder a la base de datos local y los mappers
 * para convertir entre entidades Room y modelos de dominio.
 *
 * @property foodPlaceDao DAO de Room para operaciones con establecimientos.
 */
class FoodPlaceRepositoryImpl(
    private val foodPlaceDao: FoodPlaceDao
) : FoodPlaceRepository {

    /**
     * Obtiene todos los establecimientos como un flujo reactivo de modelos de dominio.
     *
     * @return [Flow] que emite la lista actualizada de [FoodPlace].
     */
    override fun getAll(): Flow<List<FoodPlace>> {
        return foodPlaceDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Obtiene los establecimientos filtrados por categoría como un flujo reactivo.
     *
     * Convierte el enum [FoodCategory] a String para la consulta Room.
     *
     * @param category Categoría de comida por la cual filtrar.
     * @return [Flow] que emite la lista filtrada de [FoodPlace].
     */
    override fun getByCategory(category: FoodCategory): Flow<List<FoodPlace>> {
        return foodPlaceDao.getByCategory(category.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Obtiene un establecimiento por su identificador.
     *
     * @param id Identificador único del establecimiento.
     * @return El [FoodPlace] encontrado, o null si no existe.
     */
    override suspend fun getById(id: Long): FoodPlace? {
        return foodPlaceDao.getById(id)?.toDomain()
    }

    /**
     * Inserta un nuevo establecimiento de comida.
     *
     * @param foodPlace Datos del establecimiento a insertar.
     * @return El ID auto-generado del establecimiento insertado.
     */
    override suspend fun insert(foodPlace: FoodPlace): Long {
        return foodPlaceDao.insert(foodPlace.toEntity())
    }

    /**
     * Actualiza un establecimiento de comida existente.
     *
     * @param foodPlace Datos actualizados del establecimiento.
     */
    override suspend fun update(foodPlace: FoodPlace) {
        foodPlaceDao.update(foodPlace.toEntity())
    }

    /**
     * Elimina un establecimiento de comida.
     *
     * @param foodPlace El establecimiento a eliminar.
     */
    override suspend fun delete(foodPlace: FoodPlace) {
        foodPlaceDao.delete(foodPlace.toEntity())
    }

    /**
     * Obtiene una lista de establecimientos por sus identificadores.
     *
     * @param ids Lista de identificadores a buscar.
     * @return Lista de [FoodPlace] encontrados.
     */
    override suspend fun getByIds(ids: List<Long>): List<FoodPlace> {
        return foodPlaceDao.getByIds(ids).map { it.toDomain() }
    }
}
