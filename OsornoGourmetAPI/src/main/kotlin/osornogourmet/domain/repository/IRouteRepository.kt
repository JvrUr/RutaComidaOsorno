package osornogourmet.domain.repository

import osornogourmet.domain.model.Route

interface IRouteRepository {
    suspend fun findAll(): List<Route>
    suspend fun findById(id: Long): Route?
    suspend fun insert(route: Route): Long
    suspend fun update(id: Long, route: Route): Boolean
    suspend fun delete(id: Long): Boolean
}
