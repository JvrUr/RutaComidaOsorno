package com.example.osornogourmet.domain.usecase.route

import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository

/**
 * Caso de uso para obtener una ruta gastronómica por su identificador.
 *
 * @property routeRepository Repositorio de rutas.
 */
class GetRouteByIdUseCase(
    private val routeRepository: RouteRepository
) {

    /**
     * Obtiene una ruta por su identificador.
     *
     * @param id Identificador único de la ruta.
     * @return La [Route] encontrada, o null si no existe.
     */
    suspend operator fun invoke(id: Long): Route? {
        return routeRepository.getById(id)
    }
}
