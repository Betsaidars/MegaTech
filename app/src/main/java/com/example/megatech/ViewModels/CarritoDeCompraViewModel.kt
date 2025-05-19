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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CarritoDeCompraViewModel(private val context: Context): ViewModel() {

    private val gson = Gson()
    private val sharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)

    private val _itemCarrito = MutableStateFlow<List<ItemsModel>>(loadCarritolist())
    val itemCarrito: StateFlow<List<ItemsModel>> = _itemCarrito

    private val _totalPrecioSeleccionado = MutableStateFlow(0.0)
    val totalPrecioSeleccionado: StateFlow<Double> = _totalPrecioSeleccionado


    init {
        Log.d("CarritoCompraInit", "Lista cargada al inicio con ${_itemCarrito.value.size} items")
        actualizarTotal()  // <-- Agregado para actualizar el total en inicio
        viewModelScope.launch {
            itemCarrito.collectLatest { saveCarritolist(it) }
        }
    }


    fun addItemToCarritolist(item: ItemsModel) {
        // Crear una copia del item con cantidad 1 si es cero o menor
        val itemConCantidad = if (item.cantidad <= 0) item.copy(cantidad = 1) else item

        if (!_itemCarrito.value.none { it.id == itemConCantidad.id }) return
        val updatedList = _itemCarrito.value + itemConCantidad
        _itemCarrito.value = updatedList
        actualizarTotal()
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
            gson.fromJson<List<ItemsModel>>(it, type) ?: emptyList()
        } ?: emptyList()
    }

    fun borrarCarrito() {
        _itemCarrito.update { emptyList() }
        // Limpiar también de SharedPreferences
        sharedPreferences.edit().remove("carritolist").apply()
        println("Carrito borrado y limpiado de SharedPreferences") // Para depuración
        Log.d("CarritoViewModel", "Carrito borrado y limpiado de SharedPreferences")
    }

    fun recargarCarrito() {
        _itemCarrito.value = loadCarritolist()
        actualizarTotal()  // <-- Agregado para actualizar total al recargar
        Log.d("CarritoViewModel", "Carrito recargado desde SharedPreferences con ${_itemCarrito.value.size} items")
    }

    fun incrementarCantidad(item: ItemsModel) {
        val updatedList = _itemCarrito.value.map {
            if (it.id == item.id) it.copy(cantidad = (it.cantidad ?: 1) + 1) else it
        }
        _itemCarrito.value = updatedList
        actualizarTotal()
    }

    fun decrementarCantidad(item: ItemsModel) {
        val updatedList = _itemCarrito.value.map {
            if (it.id == item.id && (it.cantidad ?: 1) > 1)
                it.copy(cantidad = (it.cantidad ?: 1) - 1)
            else it
        }
        _itemCarrito.value = updatedList
        actualizarTotal()
    }

    fun actualizarTotal() {
        _totalPrecioSeleccionado.value = _itemCarrito.value.sumOf { it.price * (it.cantidad ?: 1) }
    }





}