package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para agregar un nuevo establecimiento de comida.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class AddFoodPlaceUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Inserta un nuevo establecimiento en el sistema.
     *
     * @param foodPlace Datos del establecimiento a agregar.
     * @return El identificador único asignado al nuevo establecimiento.
     */
    suspend operator fun invoke(foodPlace: FoodPlace): Long {
        return foodPlaceRepository.insert(foodPlace)
    }
}
