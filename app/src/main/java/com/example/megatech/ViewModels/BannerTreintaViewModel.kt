package com.example.megatech.ViewModels

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.megatech.Model.ItemsWithDiscountModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BannerTreintaViewModel(private val sessionManager: SessionManager) : ViewModel() {


    private val _discountedItemsTreinta = MutableStateFlow<List<ItemsWithDiscountModel>>(emptyList())
    val discountedItemsTreinta: StateFlow<List<ItemsWithDiscountModel>> = _discountedItemsTreinta

    private val _isLoadingDiscountedItemsTreinta = MutableStateFlow(false)
    val isLoadingDiscountedItemsTreinta: StateFlow<Boolean> = _isLoadingDiscountedItemsTreinta

    private val _errorLoadingDiscountedItemsTreinta = MutableStateFlow<String?>(null)
    val errorLoadingDiscountedItemsTreinta: StateFlow<String?> = _errorLoadingDiscountedItemsTreinta


    init {
        getDiscountedItemsTreinta()
    }

    @OptIn(UnstableApi::class)
    fun getDiscountedItemsTreinta() {
        viewModelScope.launch {
            _isLoadingDiscountedItemsTreinta.value = true
            _errorLoadingDiscountedItemsTreinta.value = null
            try {
                val response = RetrofitCliente.apiService.getDiscountedItems()
                if (response.isSuccessful) {
                    _discountedItemsTreinta.value = (response.body() ?: emptyList()) as List<ItemsWithDiscountModel>
                    Log.d("MainViewModel", "Items con descuento cargados: ${_discountedItemsTreinta.value.size}")
                } else {
                    _errorLoadingDiscountedItemsTreinta.value = "Error al cargar los items con descuento: ${response.code()}"
                    Log.d("MainViewModel", "Error en la API al obtener items con descuento: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorLoadingDiscountedItemsTreinta.value = "Error al obtener los items con descuento: ${e.message}"
                Log.e("MainViewModel", "Error al obtener los items con descuento: ${e.message}")
            } finally {
                _isLoadingDiscountedItemsTreinta.value = false
            }
        }
    }






}