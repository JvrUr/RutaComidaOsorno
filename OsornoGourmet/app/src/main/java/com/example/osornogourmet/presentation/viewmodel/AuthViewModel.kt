package com.example.osornogourmet.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.osornogourmet.domain.model.User
import com.example.osornogourmet.domain.usecase.auth.LoginUseCase
import com.example.osornogourmet.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la UI de autenticación
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

/**
 * ViewModel para autenticación de usuarios (Login/Registro)
 * Patrón Observer: StateFlow -> UI reactiva
 */
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    /**
     * Iniciar sesión con email y contraseña
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                val user = loginUseCase(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _uiState.value = AuthUiState(isSuccess = true)
                } else {
                    _uiState.value = AuthUiState(error = "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = "Error al iniciar sesión: ${e.message}")
            }
        }
    }

    /**
     * Registrar nuevo usuario
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            try {
                val result = registerUseCase(name, email, password)
                result.fold(
                    onSuccess = {
                        // Iniciar sesión automáticamente tras registro exitoso
                        val user = loginUseCase(email, password)
                        _currentUser.value = user
                        _uiState.value = AuthUiState(isSuccess = true)
                    },
                    onFailure = { e ->
                        _uiState.value = AuthUiState(error = e.message ?: "Error al registrarse")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = AuthUiState(error = "Error al registrarse: ${e.message}")
            }
        }
    }

    /**
     * Cerrar sesión
     */
    fun logout() {
        _currentUser.value = null
        _uiState.value = AuthUiState()
    }

    /**
     * Limpiar errores
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Reiniciar estado
     */
    fun resetState() {
        _uiState.value = AuthUiState()
    }

    /**
     * Factory para crear AuthViewModel (patrón Factory)
     */
    class Factory(
        private val loginUseCase: LoginUseCase,
        private val registerUseCase: RegisterUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(loginUseCase, registerUseCase) as T
        }
    }
}
