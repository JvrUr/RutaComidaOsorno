package com.example.osornogourmet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.model.Route
import com.example.osornogourmet.domain.usecase.foodplace.GetFoodPlacesByIdsUseCase
import com.example.osornogourmet.domain.usecase.foodplace.GetFoodPlacesUseCase
import com.example.osornogourmet.domain.usecase.route.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión de rutas gastronómicas
 * Patrón Observer: StateFlow -> UI reactiva
 */
class RouteViewModel(
    private val getRoutesUseCase: GetRoutesUseCase,
    private val getRoutesByUserUseCase: GetRoutesByUserUseCase,
    private val getRouteByIdUseCase: GetRouteByIdUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val updateRouteUseCase: UpdateRouteUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val getFoodPlacesByIdsUseCase: GetFoodPlacesByIdsUseCase,
    private val getFoodPlacesUseCase: GetFoodPlacesUseCase
) : ViewModel() {

    private val _routes = MutableStateFlow<List<Route>>(emptyList())
    val routes: StateFlow<List<Route>> = _routes.asStateFlow()

    private val _allFoodPlaces = MutableStateFlow<List<FoodPlace>>(emptyList())
    val allFoodPlaces: StateFlow<List<FoodPlace>> = _allFoodPlaces.asStateFlow()

    private val _routeFoodPlaces = MutableStateFlow<List<FoodPlace>>(emptyList())
    val routeFoodPlaces: StateFlow<List<FoodPlace>> = _routeFoodPlaces.asStateFlow()

    private val _selectedRoute = MutableStateFlow<Route?>(null)
    val selectedRoute: StateFlow<Route?> = _selectedRoute.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadRoutes()
        loadAllFoodPlaces()
    }

    /**
     * Cargar todas las rutas
     */
    fun loadRoutes() {
        viewModelScope.launch {
            try {
                getRoutesUseCase().collect { routeList ->
                    _routes.value = routeList
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar rutas: ${e.message}"
            }
        }
    }

    /**
     * Cargar todos los locales disponibles (para el formulario de ruta)
     */
    private fun loadAllFoodPlaces() {
        viewModelScope.launch {
            try {
                getFoodPlacesUseCase().collect { places ->
                    _allFoodPlaces.value = places
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar locales: ${e.message}"
            }
        }
    }

    /**
     * Cargar locales de una ruta específica
     */
    fun loadRouteFoodPlaces(foodPlaceIds: List<Long>) {
        viewModelScope.launch {
            try {
                _routeFoodPlaces.value = getFoodPlacesByIdsUseCase(foodPlaceIds)
            } catch (e: Exception) {
                _error.value = "Error al cargar locales de ruta: ${e.message}"
            }
        }
    }

    /**
     * Cargar ruta por ID
     */
    fun loadRouteById(id: Long) {
        viewModelScope.launch {
            try {
                _selectedRoute.value = getRouteByIdUseCase(id)
                _selectedRoute.value?.let { route ->
                    loadRouteFoodPlaces(route.foodPlaceIds)
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar ruta: ${e.message}"
            }
        }
    }

    /**
     * Crear nueva ruta gastronómica
     */
    fun createRoute(route: Route, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                createRouteUseCase(route)
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al crear ruta: ${e.message}"
            }
        }
    }

    /**
     * Actualizar ruta existente
     */
    fun updateRoute(route: Route, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                updateRouteUseCase(route)
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al actualizar ruta: ${e.message}"
            }
        }
    }

    /**
     * Eliminar ruta
     */
    fun deleteRoute(route: Route, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                deleteRouteUseCase(route)
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al eliminar ruta: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    /**
     * Factory para crear RouteViewModel (patrón Factory)
     */
    class Factory(
        private val getRoutesUseCase: GetRoutesUseCase,
        private val getRoutesByUserUseCase: GetRoutesByUserUseCase,
        private val getRouteByIdUseCase: GetRouteByIdUseCase,
        private val createRouteUseCase: CreateRouteUseCase,
        private val updateRouteUseCase: UpdateRouteUseCase,
        private val deleteRouteUseCase: DeleteRouteUseCase,
        private val getFoodPlacesByIdsUseCase: GetFoodPlacesByIdsUseCase,
        private val getFoodPlacesUseCase: GetFoodPlacesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RouteViewModel(
                getRoutesUseCase, getRoutesByUserUseCase, getRouteByIdUseCase,
                createRouteUseCase, updateRouteUseCase, deleteRouteUseCase,
                getFoodPlacesByIdsUseCase, getFoodPlacesUseCase
            ) as T
        }
    }
}
