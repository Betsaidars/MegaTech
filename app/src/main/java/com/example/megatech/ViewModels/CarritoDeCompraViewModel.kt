package com.example.megatech.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.megatech.Model.ItemsModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CarritoDeCompraViewModel(private val context: Context): ViewModel() {

    private val gson = Gson()
    private val sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
    private val _itemCarrito = MutableStateFlow<List<ItemsModel>>(loadCarritolist())
    val itemCarrito: StateFlow<List<ItemsModel>> = _itemCarrito

    init {
        Log.d("CarritoCompraInit", "Lista cargada al inicio con ${_itemCarrito.value.size} items")
        viewModelScope.launch {
            itemCarrito.collectLatest { saveCarritolist(it) }
        }
    }

    fun addItemToCarritolist(item: ItemsModel) {
        if (!_itemCarrito.value.none { it.id == item.id }) return
        val updatedList = _itemCarrito.value + item
        _itemCarrito.value = updatedList // Esto emite el nuevo estado
        Log.d("CarritolistFlow", "Emitiendo nueva lista al agregar: ${updatedList.size} items")
    }

    fun removeItemFromCarritolist(item: ItemsModel) {
        val updatedList = _itemCarrito.value.filter { it.id != item.id }
        _itemCarrito.value = updatedList // Esto emite el nuevo estado
        Log.d("CarritolistFlow", "Emitiendo nueva lista al eliminar: ${updatedList.size} items")
    }

    fun isItemInCarritolist(itemId: String?): Boolean {
        return _itemCarrito.value.any { it.id == itemId }
    }

    private fun saveCarritolist(items: List<ItemsModel>) {
        val jsonString = gson.toJson(items)
        sharedPreferences.edit().putString("carritolist", jsonString).apply()
    }

    private fun loadCarritolist(): List<ItemsModel> {
        val jsonString = sharedPreferences.getString("carritolist", null)
        return jsonString?.let {
            val type = object : TypeToken<List<ItemsModel>>() {}.type
            gson.fromJson<List<ItemsModel>>(it, type) // <--- Aquí ocurre el NullPointerException
        } ?: emptyList()
    }





}