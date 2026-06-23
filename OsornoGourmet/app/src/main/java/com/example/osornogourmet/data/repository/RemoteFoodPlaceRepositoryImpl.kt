package com.example.osornogourmet.data.repository

import com.example.osornogourmet.data.remote.GourmetApi
import com.example.osornogourmet.data.remote.dto.toDomain
import com.example.osornogourmet.data.remote.dto.toDto
import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteFoodPlaceRepositoryImpl(
    private val api: GourmetApi
) : FoodPlaceRepository {

    override fun getAll(): Flow<List<FoodPlace>> = flow {
        emit(api.getFoodPlaces().map { it.toDomain() })
    }

    override fun getByCategory(category: FoodCategory): Flow<List<FoodPlace>> = flow {
        emit(api.getFoodPlacesByCategory(category.name).map { it.toDomain() })
    }

    override suspend fun getById(id: Long): FoodPlace? {
        return try {
            api.getFoodPlaceById(id).toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun insert(foodPlace: FoodPlace): Long {
        return try {
            api.createFoodPlace(foodPlace.toDto()).id ?: -1L
        } catch (e: Exception) {
            e.printStackTrace()
            -1L
        }
    }

    override suspend fun update(foodPlace: FoodPlace) {
        try {
            api.updateFoodPlace(foodPlace.id, foodPlace.toDto())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(foodPlace: FoodPlace) {
        try {
            api.deleteFoodPlace(foodPlace.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getByIds(ids: List<Long>): List<FoodPlace> {
        return try {
            api.getFoodPlacesBatch(ids).map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
