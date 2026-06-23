package com.example.osornogourmet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.osornogourmet.data.remote.OpenRouteServiceApi
import com.example.osornogourmet.data.remote.OrsDirectionsRequest
import com.example.osornogourmet.data.remote.PolylineDecoder
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.usecase.foodplace.GetFoodPlacesByIdsUseCase
import com.example.osornogourmet.domain.usecase.foodplace.GetFoodPlacesUseCase
import com.example.osornogourmet.domain.usecase.route.GetRouteByIdUseCase
import com.example.osornogourmet.domain.usecase.route.GetRoutesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado del mapa
 */
data class MapUiState(
    val allFoodPlaces: List<FoodPlace> = emptyList(),
    val routeFoodPlaces: List<FoodPlace> = emptyList(),
    val routePolyline: List<Pair<Double, Double>> = emptyList(), // lat, lng
    val selectedRoute: Route? = null,
    val routes: List<Route> = emptyList(),
    val isLoadingRoute: Boolean = false,
    val routeDistance: String = "",
    val routeDuration: String = "",
    val error: String? = null
)

/**
 * ViewModel para el mapa con OpenStreetMap y OpenRouteService
 * Gestiona marcadores, polylines y trazado de rutas
 */
class MapViewModel(
    private val getFoodPlacesUseCase: GetFoodPlacesUseCase,
    private val getRoutesUseCase: GetRoutesUseCase,
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val getFoodPlacesByIdsUseCase: GetFoodPlacesByIdsUseCase,
    private val orsApi: OpenRouteServiceApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    // API key de OpenRouteService (gratuita)
    // Registrarse en https://openrouteservice.org para obtener una
    private val orsApiKey = "eyJvcmciOiI1YjNjZTM1OTc4NTExMTAwMDFjZjYyNDgiLCJpZCI6ImQ1ZWQ3MDMzZWMxZDRlNzliZDg3N2EwZDY5ZTVlMTE5IiwiaCI6Im11cm11cjY0In0="

    init {
        loadAllFoodPlaces()
        loadAllRoutes()
    }

    /**
     * Cargar todos los locales para mostrar como marcadores
     */
    private fun loadAllFoodPlaces() {
        viewModelScope.launch {
            try {
                getFoodPlacesUseCase().collect { places ->
                    _uiState.value = _uiState.value.copy(allFoodPlaces = places)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error al cargar locales: ${e.message}")
            }
        }
    }

    /**
     * Cargar todas las rutas disponibles
     */
    private fun loadAllRoutes() {
        viewModelScope.launch {
            try {
                getRoutesUseCase().collect { routes ->
                    _uiState.value = _uiState.value.copy(routes = routes)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Error al cargar rutas: ${e.message}")
            }
        }
    }

    /**
     * Seleccionar una ruta y trazar en el mapa
     */
    fun selectRoute(routeId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingRoute = true)
            try {
                val route = getRouteByIdUseCase(routeId)
                if (route != null) {
                    val foodPlaces = getFoodPlacesByIdsUseCase(route.foodPlaceIds)
                    _uiState.value = _uiState.value.copy(
                        selectedRoute = route,
                        routeFoodPlaces = foodPlaces
                    )
                    // Trazar ruta con OpenRouteService
                    if (foodPlaces.size >= 2) {
                        traceRoute(foodPlaces)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingRoute = false,
                    error = "Error al cargar ruta: ${e.message}"
                )
            }
        }
    }

    /**
     * Trazar ruta entre locales usando OpenRouteService API
     * ORS usa formato [longitude, latitude]
     */
    private suspend fun traceRoute(foodPlaces: List<FoodPlace>) {
        try {
            val coordinates = foodPlaces.map { listOf(it.longitude, it.latitude) }
            val request = OrsDirectionsRequest(coordinates = coordinates)
            val response = orsApi.getDirections(orsApiKey, request)

            if (response.routes.isNotEmpty()) {
                val route = response.routes[0]
                // Decodificar polyline
                val polylinePoints = PolylineDecoder.decode(route.geometry)

                // Formatear distancia y duración
                val distanceKm = route.summary.distance / 1000.0
                val durationMin = route.summary.duration / 60.0

                _uiState.value = _uiState.value.copy(
                    routePolyline = polylinePoints,
                    routeDistance = String.format("%.1f km", distanceKm),
                    routeDuration = String.format("%.0f min", durationMin),
                    isLoadingRoute = false
                )
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoadingRoute = false,
                error = "Error al trazar ruta: ${e.message}. ¿Configuraste tu API key de OpenRouteService?"
            )
        }
    }

    /**
     * Limpiar la ruta seleccionada
     */
    fun clearRoute() {
        _uiState.value = _uiState.value.copy(
            selectedRoute = null,
            routeFoodPlaces = emptyList(),
            routePolyline = emptyList(),
            routeDistance = "",
            routeDuration = ""
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Factory para crear MapViewModel (patrón Factory)
     */
    class Factory(
        private val getFoodPlacesUseCase: GetFoodPlacesUseCase,
        private val getRoutesUseCase: GetRoutesUseCase,
        private val getRouteByIdUseCase: GetRouteByIdUseCase,
        private val getFoodPlacesByIdsUseCase: GetFoodPlacesByIdsUseCase,
        private val orsApi: OpenRouteServiceApi
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(
                getFoodPlacesUseCase, getRoutesUseCase, getRouteByIdUseCase,
                getFoodPlacesByIdsUseCase, orsApi
            ) as T
        }
    }
}
