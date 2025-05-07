package com.example.megatech.ViewModels

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.megatech.Model.ItemsWithDiscountModel
import com.example.megatech.Model.ItemsWithFiftyDiscointModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BannerCincuentaViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _discountedItemsCincuenta = MutableStateFlow<List<ItemsWithFiftyDiscointModel>>(emptyList())
    val discountedItemsCincuenta: StateFlow<List<ItemsWithFiftyDiscointModel>> = _discountedItemsCincuenta

    private val _isLoadingDiscountedItemsCincuenta = MutableStateFlow(false)
    val isLoadingDiscountedItemsCincuenta: StateFlow<Boolean> = _isLoadingDiscountedItemsCincuenta

    private val _errorLoadingDiscountedItemsCincuenta = MutableStateFlow<String?>(null)
    val errorLoadingDiscountedItemsCincuenta: StateFlow<String?> = _errorLoadingDiscountedItemsCincuenta

    init {
        getDiscountedItemsCincuenta()
    }

    @OptIn(UnstableApi::class)
    fun getDiscountedItemsCincuenta() {
        viewModelScope.launch {
            _isLoadingDiscountedItemsCincuenta.value = true
            _errorLoadingDiscountedItemsCincuenta.value = null
            try {
                val response = RetrofitCliente.apiService.getDiscountedFiftyItems()
                if (response.isSuccessful) {
                    _discountedItemsCincuenta.value = (response.body() ?: emptyList()) as List<ItemsWithFiftyDiscointModel >
                    Log.d("MainViewModel", "Items con descuento cargados: ${_discountedItemsCincuenta.value.size}")
                } else {
                    _errorLoadingDiscountedItemsCincuenta.value = "Error al cargar los items con descuento: ${response.code()}"
                    Log.d("MainViewModel", "Error en la API al obtener items con descuento: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorLoadingDiscountedItemsCincuenta.value = "Error al obtener los items con descuento: ${e.message}"
                Log.e("MainViewModel", "Error al obtener los items con descuento: ${e.message}")
            } finally {
                _isLoadingDiscountedItemsCincuenta.value = false
            }
        }
    }




}