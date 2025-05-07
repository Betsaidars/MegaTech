package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.megatech.SessionManager

class BannerCincuentaViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BannerCincuentaViewModel::class.java)) {
            // Crea la instancia de LoginViewModel pasando sessionManager
            return BannerCincuentaViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}