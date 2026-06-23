package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para obtener un establecimiento de comida por su identificador.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class GetFoodPlaceByIdUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Obtiene un establecimiento por su identificador.
     *
     * @param id Identificador único del establecimiento.
     * @return El [FoodPlace] encontrado, o null si no existe.
     */
    suspend operator fun invoke(id: Long): FoodPlace? {
        return foodPlaceRepository.getById(id)
    }
}
