package com.example.megatech.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.BannerModel
import com.example.megatech.Model.ItemsModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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


    private val _televisores = MutableStateFlow<List<ItemsModel>>(emptyList())
    val televisores: StateFlow<List<ItemsModel>> = _televisores

    private val _sonido = MutableStateFlow<List<ItemsModel>>(emptyList())
    val sonido: StateFlow<List<ItemsModel>> = _sonido

    private val _phone = MutableStateFlow<List<ItemsModel>>(emptyList())
    val phone: StateFlow<List<ItemsModel>> = _phone

    private val _camaras = MutableStateFlow<List<ItemsModel>>(emptyList())
    val camaras: StateFlow<List<ItemsModel>> = _camaras

    private val _consolas = MutableStateFlow<List<ItemsModel>>(emptyList())
    val consolas: StateFlow<List<ItemsModel>> = _consolas

    private val _hogarInteligente = MutableStateFlow<List<ItemsModel>>(emptyList())
    val hogarInteligente: StateFlow<List<ItemsModel>> = _hogarInteligente

    private val _ordenadores = MutableStateFlow<List<ItemsModel>>(emptyList())
    val ordenadores: StateFlow<List<ItemsModel>> = _ordenadores

    private val _relojes = MutableStateFlow<List<ItemsModel>>(emptyList())
    val relojes: StateFlow<List<ItemsModel>> = _relojes

    init {
        getAllItems()
        filterItemsByCategory()
    }

    private fun filterItemsByCategory() {
        viewModelScope.launch {
            items.collectLatest { allItems ->
                _televisores.value = allItems.filter { it.categoryId == "6" }
                _sonido.value = allItems.filter { it.categoryId == "3" }
                _phone.value = allItems.filter { it.categoryId == "2" }
                _camaras.value = allItems.filter { it.categoryId == "5" }
                _consolas.value = allItems.filter { it.categoryId == "4" }
                _hogarInteligente.value = allItems.filter { it.categoryId == "7" }
                _ordenadores.value = allItems.filter { it.categoryId == "1" }
                _relojes.value = allItems.filter { it.categoryId == "8" }
            }
        }
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