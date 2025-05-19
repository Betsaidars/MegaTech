package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.UserModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    fun loginUser(user: UserModel, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitCliente.apiService.login(user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val message = responseBody?.get("message") as? String
                    val userId = responseBody?.get("id") as? String
                    val username = responseBody?.get("username") as? String
                    val email = responseBody?.get("email") as? String

                    if (message == "Inicio de sesión exitoso" && userId != null && username != null && email != null) {
                        // Inicio de sesión exitoso
                        sessionManager.clearSession() // Limpiar la sesión anterior
                        val loggedInUser = UserModel(id = userId, username = username, email = email, password = "") // No guardar la contraseña
                        sessionManager.saveUserModel(loggedInUser)
                        _loginResult.value = message
                        _loginError.value = null
                        onSuccess()
                    } else {
                        // Credenciales incorrectas o datos faltantes
                        _loginError.value = "Credenciales incorrectas"
                        _loginResult.value = null
                        onFailure("Credenciales incorrectas")
                    }
                } else {
                    // Error de la API
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _loginError.value = "Error de inicio de sesión: $errorBody"
                    _loginResult.value = null
                    onFailure("Error de inicio de sesión: $errorBody")
                }
            } catch (e: Exception) {
                // Error de red o excepción
                _loginError.value = "Excepción de inicio de sesión: ${e.message}"
                _loginResult.value = null
                onFailure("Excepción de inicio de sesión: ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.getUserModel() != null
    }

    fun logoutUser() {
        sessionManager.clearSession()
        _loginResult.value = null
        _loginError.value = null
    }
}