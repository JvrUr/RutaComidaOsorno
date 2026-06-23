package com.example.osornogourmet.data.repository

import com.example.osornogourmet.data.remote.GourmetApi
import com.example.osornogourmet.data.remote.dto.toDomain
import com.example.osornogourmet.data.remote.dto.toDto
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteRouteRepositoryImpl(
    private val api: GourmetApi
) : RouteRepository {

    override fun getAll(): Flow<List<Route>> = flow {
        emit(api.getRoutes().map { it.toDomain() })
    }

    override fun getByUserId(userId: Long): Flow<List<Route>> = flow {
        emit(api.getRoutesByUserId(userId).map { it.toDomain() })
    }

    override suspend fun getById(id: Long): Route? {
        return try {
            api.getRouteById(id).toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun insert(route: Route): Long {
        return try {
            api.createRoute(route.toDto())
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    override suspend fun update(route: Route) {
        try {
            api.updateRoute(route.id, route.toDto())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(route: Route) {
        try {
            api.deleteRoute(route.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
