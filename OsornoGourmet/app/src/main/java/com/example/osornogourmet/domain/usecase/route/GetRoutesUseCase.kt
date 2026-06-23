package com.example.osornogourmet.domain.usecase.route

import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener todas las rutas gastronómicas.
 *
 * @property routeRepository Repositorio de rutas.
 */
class GetRoutesUseCase(
    private val routeRepository: RouteRepository
) {

    /**
     * Obtiene todas las rutas como un flujo reactivo.
     *
     * @return [Flow] que emite la lista actualizada de rutas.
     */
    operator fun invoke(): Flow<List<Route>> {
        return routeRepository.getAll()
    }
}
