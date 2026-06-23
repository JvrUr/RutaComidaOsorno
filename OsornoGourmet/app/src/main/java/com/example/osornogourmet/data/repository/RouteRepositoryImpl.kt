package com.example.osornogourmet.data.repository

import com.example.osornogourmet.data.local.dao.RouteDao
import com.example.osornogourmet.data.local.toDomain
import com.example.osornogourmet.data.local.toEntity
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de rutas gastronómicas.
 *
 * Utiliza [RouteDao] para acceder a la base de datos local y los mappers
 * para convertir entre entidades Room y modelos de dominio. Los foodPlaceIds
 * se serializan/deserializan automáticamente entre List<Long> y JSON String.
 *
 * @property routeDao DAO de Room para operaciones con rutas.
 */
class RouteRepositoryImpl(
    private val routeDao: RouteDao
) : RouteRepository {

    /**
     * Obtiene todas las rutas gastronómicas como un flujo reactivo de modelos de dominio.
     *
     * @return [Flow] que emite la lista actualizada de [Route].
     */
    override fun getAll(): Flow<List<Route>> {
        return routeDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Obtiene las rutas creadas por un usuario específico como un flujo reactivo.
     *
     * @param userId Identificador del usuario creador.
     * @return [Flow] que emite la lista de [Route] del usuario.
     */
    override fun getByUserId(userId: Long): Flow<List<Route>> {
        return routeDao.getByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Obtiene una ruta por su identificador.
     *
     * @param id Identificador único de la ruta.
     * @return La [Route] encontrada, o null si no existe.
     */
    override suspend fun getById(id: Long): Route? {
        return routeDao.getById(id)?.toDomain()
    }

    /**
     * Inserta una nueva ruta gastronómica.
     *
     * @param route Datos de la ruta a insertar.
     * @return El ID auto-generado de la ruta insertada.
     */
    override suspend fun insert(route: Route): Long {
        return routeDao.insert(route.toEntity())
    }

    /**
     * Actualiza una ruta gastronómica existente.
     *
     * @param route Datos actualizados de la ruta.
     */
    override suspend fun update(route: Route) {
        routeDao.update(route.toEntity())
    }

    /**
     * Elimina una ruta gastronómica.
     *
     * @param route La ruta a eliminar.
     */
    override suspend fun delete(route: Route) {
        routeDao.delete(route.toEntity())
    }
}
