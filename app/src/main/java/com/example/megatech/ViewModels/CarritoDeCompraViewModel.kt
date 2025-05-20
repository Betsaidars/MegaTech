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
import kotlinx.coroutines.flow.map // Asegúrate de que esta importación esté ahí
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
        actualizarTotal()
        viewModelScope.launch {
            itemCarrito.collectLatest { saveCarritolist(it) }
        }
    }

    fun addItemToCarritolist(item: ItemsModel) {
        // Crear una copia del item con cantidad 1 si es cero o menor
        val itemConCantidad = if ((item.cantidad ?: 0) <= 0) item.copy(cantidad = 1) else item

        // BUSCAR SI YA EXISTE UN ITEM CON EL MISMO ID Y EL MISMO selectedColor
        val existingItemIndex = _itemCarrito.value.indexOfFirst {
            it.id == itemConCantidad.id && it.selectedColor == itemConCantidad.selectedColor // <-- ESTO ES CLAVE
        }

        if (existingItemIndex != -1) {
            // Si el ítem ya existe (mismo ID y mismo color), incrementa su cantidad
            val updatedList = _itemCarrito.value.toMutableList() // Convertir a mutable para modificar
            val existingItem = updatedList[existingItemIndex]
            updatedList[existingItemIndex] = existingItem.copy(cantidad = (existingItem.cantidad ?: 1) + 1)
            _itemCarrito.value = updatedList
            Log.d("CarritoViewModel", "Cantidad incrementada para item: ${itemConCantidad.name}, Color: ${itemConCantidad.selectedColor}")
        } else {
            // Si el ítem no existe (por ID y color), añádelo como un nuevo ítem
            val updatedList = _itemCarrito.value + itemConCantidad
            _itemCarrito.value = updatedList
            Log.d("CarritoViewModel", "Nuevo item añadido: ${itemConCantidad.name}, Color: ${itemConCantidad.selectedColor}")
        }
        actualizarTotal()
    }


    fun removeItemFromCarritolist(item: ItemsModel) {
        // Asegúrate de filtrar por ID y color para eliminar el ítem específico
        val updatedList = _itemCarrito.value.filter {
            !(it.id == item.id && it.selectedColor == item.selectedColor)
        }
        _itemCarrito.value = updatedList
        Log.d("CarritolistFlow", "Emitiendo nueva lista al eliminar: ${updatedList.size} items")
        actualizarTotal() // Actualizar el total después de eliminar
    }

    fun isItemInCarritolist(itemId: String?, selectedColor: String?): Boolean {
        // Ahora también se compara por color
        return _itemCarrito.value.any { it.id == itemId && it.selectedColor == selectedColor }
    }


    fun incrementarCantidad(item: ItemsModel) {
        val updatedList = _itemCarrito.value.map {
            if (it.id == item.id && it.selectedColor == item.selectedColor) { // Comparar por ID y color
                it.copy(cantidad = (it.cantidad ?: 1) + 1)
            } else {
                it
            }
        }
        _itemCarrito.value = updatedList
        actualizarTotal()
    }

    fun decrementarCantidad(item: ItemsModel) {
        val updatedList = _itemCarrito.value.mapNotNull {
            if (it.id == item.id && it.selectedColor == item.selectedColor) { // Comparar por ID y color
                if ((it.cantidad ?: 1) > 1) {
                    it.copy(cantidad = (it.cantidad ?: 1) - 1)
                } else {
                    null // Eliminar el ítem si la cantidad llega a 1 (o menos)
                }
            } else {
                it
            }
        }
        _itemCarrito.value = updatedList
        actualizarTotal()
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
        sharedPreferences.edit().remove("carritolist").apply()
        Log.d("CarritoViewModel", "Carrito borrado y limpiado de SharedPreferences")
    }

    fun recargarCarrito() {
        _itemCarrito.value = loadCarritolist()
        actualizarTotal()
        Log.d("CarritoViewModel", "Carrito recargado desde SharedPreferences con ${_itemCarrito.value.size} items")
    }

    fun actualizarTotal() {
        _totalPrecioSeleccionado.value = _itemCarrito.value.sumOf { it.price * (it.cantidad ?: 1) }
    }
}