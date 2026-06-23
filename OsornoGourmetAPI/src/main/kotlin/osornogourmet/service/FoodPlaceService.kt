package osornogourmet.service

import osornogourmet.config.IllegalArgumentExceptionWithStatus
import osornogourmet.data.dto.FoodPlaceRequest
import osornogourmet.data.dto.FoodPlaceResponse
import osornogourmet.data.mapper.FoodPlaceMapper
import osornogourmet.domain.model.FoodPlace
import osornogourmet.domain.repository.IFoodPlaceRepository

class FoodPlaceService(private val repository: IFoodPlaceRepository) {

    suspend fun getAll(): List<FoodPlaceResponse> {
        return repository.findAll().map { FoodPlaceMapper.toResponse(it) }
    }

    suspend fun getById(id: Long): FoodPlaceResponse {
        val place = repository.findById(id) ?: throw IllegalArgumentExceptionWithStatus("FoodPlace not found")
        return FoodPlaceMapper.toResponse(place)
    }

    suspend fun getByCategory(category: String): List<FoodPlaceResponse> {
        return repository.findByCategory(category).map { FoodPlaceMapper.toResponse(it) }
    }

    suspend fun getByIds(ids: List<Long>): List<FoodPlaceResponse> {
        return repository.findByIds(ids).map { FoodPlaceMapper.toResponse(it) }
    }

    suspend fun create(userId: Long, request: FoodPlaceRequest): FoodPlaceResponse {
        val place = FoodPlace(
            name = request.name,
            description = request.description,
            category = request.category,
            address = request.address,
            latitude = request.latitude,
            longitude = request.longitude,
            imageUrl = request.imageUrl,
            createdByUserId = userId
        )
        val id = repository.insert(place)
        return getById(id)
    }

    suspend fun update(id: Long, request: FoodPlaceRequest): FoodPlaceResponse {
        val place = FoodPlace(
            name = request.name,
            description = request.description,
            category = request.category,
            address = request.address,
            latitude = request.latitude,
            longitude = request.longitude,
            imageUrl = request.imageUrl
            // createdByUserId remains the same
        )
        val updated = repository.update(id, place)
        if (!updated) throw IllegalArgumentExceptionWithStatus("FoodPlace not found to update")
        return getById(id)
    }

    suspend fun delete(id: Long) {
        val deleted = repository.delete(id)
        if (!deleted) throw IllegalArgumentExceptionWithStatus("FoodPlace not found to delete")
    }
}
