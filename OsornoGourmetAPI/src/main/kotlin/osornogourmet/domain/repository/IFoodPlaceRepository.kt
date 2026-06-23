package osornogourmet.domain.repository

import osornogourmet.domain.model.FoodPlace

interface IFoodPlaceRepository {
    suspend fun findAll(): List<FoodPlace>
    suspend fun findById(id: Long): FoodPlace?
    suspend fun findByIds(ids: List<Long>): List<FoodPlace>
    suspend fun findByCategory(category: String): List<FoodPlace>
    suspend fun insert(foodPlace: FoodPlace): Long
    suspend fun update(id: Long, foodPlace: FoodPlace): Boolean
    suspend fun delete(id: Long): Boolean
}
