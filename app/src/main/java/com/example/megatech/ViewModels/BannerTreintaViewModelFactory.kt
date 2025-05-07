package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.megatech.SessionManager

class BannerTreintaViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BannerTreintaViewModel::class.java)) {
            // Crea la instancia de LoginViewModel pasando sessionManager
            return BannerTreintaViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}