package com.example.osornogourmet.domain.usecase.route

import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository

/**
 * Caso de uso para eliminar una ruta gastronómica.
 *
 * @property routeRepository Repositorio de rutas.
 */
class DeleteRouteUseCase(
    private val routeRepository: RouteRepository
) {

    /**
     * Elimina una ruta del sistema.
     *
     * @param route La ruta a eliminar.
     */
    suspend operator fun invoke(route: Route) {
        routeRepository.delete(route)
    }
}
