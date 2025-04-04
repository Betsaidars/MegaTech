package com.example.megatech.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.ItemsModel
import com.example.megatech.RetrofitCliente
import com.example.megatech.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

class DiscountViewModel(private val sessionManager: SessionManager) : ViewModel()  {

    private val _items = MutableStateFlow<List<ItemsModel>>(emptyList())  // Lista de todos los productos
    val items: StateFlow<List<ItemsModel>> = _items

    private val _discountedItems = MutableStateFlow<List<ItemsModel>>(emptyList())  // Lista de productos con descuento
    val discountedItems: StateFlow<List<ItemsModel>> = _discountedItems



    fun getAllItems() {
        viewModelScope.launch {
            try {
                val response = RetrofitCliente.apiService.getAllItems()
                if (response.isSuccessful) {
                    val itemList = response.body() ?: emptyList()
                    _items.value = itemList
                    Log.d("DiscountViewModel", "Items cargados: ${itemList.size}")
                    // Llamar a getDiscountedItems después de cargar los items
                    // Esto requerirá que DiscountView pase el porcentaje de descuento de alguna manera
                    // Podrías tener un estado en el ViewModel para el porcentaje de descuento
                } else {
                    Log.e("DiscountViewModel", "Error en la API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("DiscountViewModel", "Error al obtener los items: ${e.message}")
            }
        }
    }

    private val _currentDiscountPercentage = MutableStateFlow<Int?>(null)
    val currentDiscountPercentage: StateFlow<Int?> = _currentDiscountPercentage

    fun setDiscountPercentage(percentage: Int) {
        _currentDiscountPercentage.value = percentage
        getDiscountedItems(percentage) // Llama a la función cuando el porcentaje se establece
    }

    // Obtener productos con descuento (30% o 50%)
    fun getDiscountedItems(discountPercentage: Int) {
        viewModelScope.launch {
            val discountedList = _items.value.map {
                val discountedPrice = (it.price * (1 - discountPercentage / 100.0)).round(2)
                it.copy(price = discountedPrice)
            }
            _discountedItems.value = discountedList
        }
    }

    fun Double.round(decimals: Int): Double {
        return "%.${decimals}f".format(this).toDouble()
    }





}