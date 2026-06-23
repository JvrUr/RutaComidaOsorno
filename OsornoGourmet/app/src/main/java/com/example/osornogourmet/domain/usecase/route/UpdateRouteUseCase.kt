package com.example.osornogourmet.domain.usecase.route

import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository

/**
 * Caso de uso para actualizar una ruta gastronómica existente.
 *
 * @property routeRepository Repositorio de rutas.
 */
class UpdateRouteUseCase(
    private val routeRepository: RouteRepository
) {

    /**
     * Actualiza los datos de una ruta existente.
     *
     * @param route Datos actualizados de la ruta.
     */
    suspend operator fun invoke(route: Route) {
        routeRepository.update(route)
    }
}
