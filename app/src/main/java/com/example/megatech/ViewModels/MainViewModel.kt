package com.example.megatech.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.BannerModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _banners = MutableStateFlow<List<BannerModel>>(emptyList())
    val banners: StateFlow<List<BannerModel>> = _banners

    fun getAllBanner() {
        viewModelScope.launch {
            try {
                val response = RetrofitCliente.apiService.getAllBanner()
                if (response.isSuccessful) {
                    _banners.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al obtener los banner: ${e.message}")
            }
        }
    }
}