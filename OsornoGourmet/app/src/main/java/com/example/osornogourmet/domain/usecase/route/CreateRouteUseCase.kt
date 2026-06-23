package com.example.osornogourmet.domain.usecase.route

import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository

/**
 * Caso de uso para crear una nueva ruta gastronómica.
 *
 * @property routeRepository Repositorio de rutas.
 */
class CreateRouteUseCase(
    private val routeRepository: RouteRepository
) {

    /**
     * Crea e inserta una nueva ruta en el sistema.
     *
     * @param route Datos de la ruta a crear.
     * @return El identificador único asignado a la nueva ruta.
     */
    suspend operator fun invoke(route: Route): Long {
        return routeRepository.insert(route)
    }
}
