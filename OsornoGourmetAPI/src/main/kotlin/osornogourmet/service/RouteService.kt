package osornogourmet.service

import osornogourmet.config.IllegalArgumentExceptionWithStatus
import osornogourmet.data.dto.RouteRequest
import osornogourmet.data.dto.RouteResponse
import osornogourmet.data.mapper.RouteMapper
import osornogourmet.domain.model.Route
import osornogourmet.domain.repository.IRouteRepository

class RouteService(private val repository: IRouteRepository) {

    suspend fun getAll(): List<RouteResponse> {
        return repository.findAll().map { RouteMapper.toResponse(it) }
    }

    suspend fun getById(id: Long): RouteResponse {
        val route = repository.findById(id) ?: throw IllegalArgumentExceptionWithStatus("Route not found")
        return RouteMapper.toResponse(route)
    }

    suspend fun create(userId: Long, request: RouteRequest): RouteResponse {
        val route = Route(
            name = request.name,
            description = request.description,
            foodPlaceIds = request.foodPlaceIds,
            createdByUserId = userId,
            estimatedDuration = "1h 30m", // Mock or calculated logic here
            estimatedDistance = "2.5 km" // Mock or calculated logic here
        )
        val id = repository.insert(route)
        return getById(id)
    }

    suspend fun update(id: Long, request: RouteRequest): RouteResponse {
        val route = Route(
            name = request.name,
            description = request.description,
            foodPlaceIds = request.foodPlaceIds,
            estimatedDuration = "1h 30m",
            estimatedDistance = "2.5 km"
        )
        val updated = repository.update(id, route)
        if (!updated) throw IllegalArgumentExceptionWithStatus("Route not found to update")
        return getById(id)
    }

    suspend fun delete(id: Long) {
        val deleted = repository.delete(id)
        if (!deleted) throw IllegalArgumentExceptionWithStatus("Route not found to delete")
    }
}
