package com.example.osornogourmet.domain.usecase.foodplace

import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.repository.FoodPlaceRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener establecimientos de comida filtrados por categoría.
 *
 * @property foodPlaceRepository Repositorio de establecimientos.
 */
class GetFoodPlacesByCategoryUseCase(
    private val foodPlaceRepository: FoodPlaceRepository
) {

    /**
     * Obtiene los establecimientos de una categoría específica como un flujo reactivo.
     *
     * @param category Categoría por la cual filtrar.
     * @return [Flow] que emite la lista filtrada de establecimientos.
     */
    operator fun invoke(category: FoodCategory): Flow<List<FoodPlace>> {
        return foodPlaceRepository.getByCategory(category)
    }
}
