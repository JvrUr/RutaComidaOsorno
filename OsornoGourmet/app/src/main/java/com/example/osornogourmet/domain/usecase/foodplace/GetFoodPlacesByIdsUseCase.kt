package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository

/**
 * Caso de uso para obtener establecimientos de comida por una lista de identificadores.
 *
 * Útil para cargar los establecimientos asociados a una ruta gastronómica.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class GetFoodPlacesByIdsUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Obtiene los establecimientos correspondientes a los identificadores proporcionados.
     *
     * @param ids Lista de identificadores de los establecimientos.
     * @return Lista de [FoodPlace] encontrados.
     */
    suspend operator fun invoke(ids: List<Long>): List<FoodPlace> {
        return foodPlaceRepository.getByIds(ids)
    }
}
