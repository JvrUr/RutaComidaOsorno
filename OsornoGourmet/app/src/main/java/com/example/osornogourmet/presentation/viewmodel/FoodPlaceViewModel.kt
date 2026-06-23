package com.example.osornogourmet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.osornogourmet.domain.model.FoodCategory
import com.example.osornogourmet.domain.model.FoodPlace
import com.example.osornogourmet.domain.usecase.foodplace.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestión de locales de comida
 * Implementa CRUD completo con filtro por categoría
 * Patrón Observer: StateFlow -> UI reactiva
 */
class FoodPlaceViewModel(
    private val getFoodPlacesUseCase: GetFoodPlacesUseCase,
    private val getFoodPlacesByCategoryUseCase: GetFoodPlacesByCategoryUseCase,
    private val getFoodPlaceByIdUseCase: GetFoodPlaceByIdUseCase,
    private val addFoodPlaceUseCase: AddFoodPlaceUseCase,
    private val updateFoodPlaceUseCase: UpdateFoodPlaceUseCase,
    private val deleteFoodPlaceUseCase: DeleteFoodPlaceUseCase
) : ViewModel() {

    private val _foodPlaces = MutableStateFlow<List<FoodPlace>>(emptyList())
    val foodPlaces: StateFlow<List<FoodPlace>> = _foodPlaces.asStateFlow()

    private val _selectedCategory = MutableStateFlow<FoodCategory?>(null)
    val selectedCategory: StateFlow<FoodCategory?> = _selectedCategory.asStateFlow()

    private val _selectedFoodPlace = MutableStateFlow<FoodPlace?>(null)
    val selectedFoodPlace: StateFlow<FoodPlace?> = _selectedFoodPlace.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadFoodPlaces()
    }

    /**
     * Cargar todos los locales de comida
     */
    fun loadFoodPlaces() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getFoodPlacesUseCase().collect { places ->
                    _foodPlaces.value = places
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar locales: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    /**
     * Filtrar locales por categoría
     */
    fun filterByCategory(category: FoodCategory?) {
        _selectedCategory.value = category
        viewModelScope.launch {
            try {
                if (category == null) {
                    getFoodPlacesUseCase().collect { places ->
                        _foodPlaces.value = places
                    }
                } else {
                    getFoodPlacesByCategoryUseCase(category).collect { places ->
                        _foodPlaces.value = places
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error al filtrar: ${e.message}"
            }
        }
    }

    /**
     * Cargar detalle de un local por ID
     */
    fun loadFoodPlaceById(id: Long) {
        viewModelScope.launch {
            try {
                _selectedFoodPlace.value = getFoodPlaceByIdUseCase(id)
            } catch (e: Exception) {
                _error.value = "Error al cargar local: ${e.message}"
            }
        }
    }

    /**
     * Agregar nuevo local de comida
     */
    fun addFoodPlace(foodPlace: FoodPlace, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                addFoodPlaceUseCase(foodPlace)
                loadFoodPlaces()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al agregar local: ${e.message}"
            }
        }
    }

    /**
     * Actualizar local existente
     */
    fun updateFoodPlace(foodPlace: FoodPlace, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                updateFoodPlaceUseCase(foodPlace)
                loadFoodPlaces()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al actualizar local: ${e.message}"
            }
        }
    }

    /**
     * Eliminar local de comida
     */
    fun deleteFoodPlace(foodPlace: FoodPlace, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                deleteFoodPlaceUseCase(foodPlace)
                loadFoodPlaces()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al eliminar local: ${e.message}"
            }
        }
    }

    /**
     * Limpiar error
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Factory para crear FoodPlaceViewModel (patrón Factory)
     */
    class Factory(
        private val getFoodPlacesUseCase: GetFoodPlacesUseCase,
        private val getFoodPlacesByCategoryUseCase: GetFoodPlacesByCategoryUseCase,
        private val getFoodPlaceByIdUseCase: GetFoodPlaceByIdUseCase,
        private val addFoodPlaceUseCase: AddFoodPlaceUseCase,
        private val updateFoodPlaceUseCase: UpdateFoodPlaceUseCase,
        private val deleteFoodPlaceUseCase: DeleteFoodPlaceUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FoodPlaceViewModel(
                getFoodPlacesUseCase, getFoodPlacesByCategoryUseCase,
                getFoodPlaceByIdUseCase, addFoodPlaceUseCase,
                updateFoodPlaceUseCase, deleteFoodPlaceUseCase
            ) as T
        }
    }
}
