package com.example.megatech.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.BannerModel
import com.example.megatech.Model.ItemsModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _banners = MutableStateFlow<List<BannerModel>>(emptyList())
    val banners: StateFlow<List<BannerModel>> = _banners

    private val _items = MutableStateFlow<List<ItemsModel>>(emptyList())
    val items: StateFlow<List<ItemsModel>> = _items

    private val _isLoadingItems = MutableStateFlow(false)
    val isLoadingItems: StateFlow<Boolean> = _isLoadingItems

    private val _errorLoadingItems = MutableStateFlow<String?>(null)
    val errorLoadingItems: StateFlow<String?> = _errorLoadingItems


    init {
        getAllItems()
    }

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


    fun getAllItems() {
        viewModelScope.launch {
            try {
                val response = RetrofitCliente.apiService.getAllItems()
                if (response.isSuccessful) {
                    val itemList = response.body() ?: emptyList()
                    _items.value = itemList
                    Log.d("MainViewModel", "Items cargados: ${itemList.size}")
                } else {
                    Log.e("MainViewModel", "Error en la API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error al obtener los items: ${e.message}")
            }
        }
    }



}