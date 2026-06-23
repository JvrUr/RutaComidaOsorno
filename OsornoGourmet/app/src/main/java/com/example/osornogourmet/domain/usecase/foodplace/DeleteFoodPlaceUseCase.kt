package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para eliminar un establecimiento de comida.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class DeleteFoodPlaceUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Elimina un establecimiento del sistema.
     *
     * @param foodPlace El establecimiento a eliminar.
     */
    suspend operator fun invoke(foodPlace: FoodPlace) {
        foodPlaceRepository.delete(foodPlace)
    }
}
