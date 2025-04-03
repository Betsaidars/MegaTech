package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.UserModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    fun loginUser(user: UserModel, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitCliente.apiService.login(user)
                if (response.isSuccessful) {
                    val message = response.body()?.get("message")
                    if (message != null && message == "Inicio de sesión exitoso") {
                        // Inicio de sesión exitoso
                        onSuccess(message)
                    } else {
                        // Credenciales incorrectas
                        onFailure("Credenciales incorrectas")
                    }
                } else {
                    // Error de la API
                    onFailure("Error de inicio de sesión: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                // Error de red o excepción
                onFailure("Excepción de inicio de sesión: ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(): Boolean {
        return sessionManager.getUserModel() != null
    }

    fun logoutUser() {
        sessionManager.clearSession()
    }


}