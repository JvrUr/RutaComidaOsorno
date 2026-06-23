package com.example.osornogourmet.domain.usecase.route

import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener las rutas gastronómicas de un usuario específico.
 *
 * @property routeRepository Repositorio de rutas.
 */
class GetRoutesByUserUseCase(
    private val routeRepository: RouteRepository
) {

    /**
     * Obtiene las rutas creadas por un usuario como un flujo reactivo.
     *
     * @param userId Identificador del usuario.
     * @return [Flow] que emite la lista de rutas del usuario.
     */
    operator fun invoke(userId: Long): Flow<List<Route>> {
        return routeRepository.getByUserId(userId)
    }
}
