package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todos los establecimientos de comida.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class GetFoodPlacesUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Obtiene todos los establecimientos como un flujo reactivo.
     *
     * @return [Flow] que emite la lista actualizada de establecimientos.
     */
    operator fun invoke(): Flow<List<FoodPlace>> {
        return foodPlaceRepository.getAll()
    }
}
