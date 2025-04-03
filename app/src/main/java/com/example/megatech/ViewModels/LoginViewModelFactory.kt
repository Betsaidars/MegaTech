package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.megatech.SessionManager

// Factory para crear el ViewModel con el parámetro SessionManager
class LoginViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {

    // El método create se utiliza para crear una instancia del ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            // Crea la instancia de LoginViewModel pasando sessionManager
            return LoginViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}