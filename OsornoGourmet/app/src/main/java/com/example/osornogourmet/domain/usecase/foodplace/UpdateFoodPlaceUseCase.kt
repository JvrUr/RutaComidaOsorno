package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para actualizar un establecimiento de comida existente.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class UpdateFoodPlaceUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Actualiza los datos de un establecimiento existente.
     *
     * @param foodPlace Datos actualizados del establecimiento.
     */
    suspend operator fun invoke(foodPlace: FoodPlace) {
        foodPlaceRepository.update(foodPlace)
    }
}
