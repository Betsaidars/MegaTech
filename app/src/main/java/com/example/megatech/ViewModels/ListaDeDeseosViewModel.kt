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

class ListaDeDeseosViewModel(private val context: Context) : ViewModel() {

    private val gson = Gson()
    private val sharedPreferences =
        context.getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)
    private val _wishlistItems = MutableStateFlow<List<ItemsModel>>(loadWishlist())
    val wishlistItems: StateFlow<List<ItemsModel>> = _wishlistItems

    private val _cartItems = MutableStateFlow<List<ItemsModel>>(emptyList())  // ADD THIS LINE
    val cartItems: StateFlow<List<ItemsModel>> = _cartItems


    init {
        Log.d("WishlistInit", "Lista cargada al inicio con ${_wishlistItems.value.size} items")
        viewModelScope.launch {
            wishlistItems.collectLatest { saveWishlist(it) }
        }
    }

    fun addItemToWishlist(item: ItemsModel) {
        // Asegúrate de que el item tenga una cantidad inicial si es necesario, aunque para deseos no suele ser crítico
        val itemToAdd = item.copy(cantidad = item.cantidad.takeIf { it > 0 } ?: 1)

        // Buscar si ya existe un item con el MISMO ID y el MISMO selectedColor
        val alreadyExists = _wishlistItems.value.any {
            it.id == itemToAdd.id && it.selectedColor == itemToAdd.selectedColor
        }

        if (!alreadyExists) { // Solo añade si no existe una combinación id+color
            val updatedList = _wishlistItems.value + itemToAdd
            _wishlistItems.value = updatedList
            Log.d("WishlistViewModel", "Item añadido a la lista de deseos: ${itemToAdd.name}, Color: ${itemToAdd.selectedColor}")
        } else {
            Log.d("WishlistViewModel", "Item ya existe en la lista de deseos: ${itemToAdd.name}, Color: ${itemToAdd.selectedColor}")
        }
    }

    fun removeItemFromWishlist(item: ItemsModel) {
        // Eliminar el ítem específico por ID y color
        val updatedList = _wishlistItems.value.filter {
            !(it.id == item.id && it.selectedColor == item.selectedColor)
        }
        _wishlistItems.value = updatedList
        Log.d("WishlistViewModel", "Item eliminado de la lista de deseos: ${item.name}, Color: ${item.selectedColor}")
    }

    fun isItemInWishlist(itemId: String?, selectedColor: String?): Boolean {
        // Verificar si existe por ID y color
        return _wishlistItems.value.any { it.id == itemId && it.selectedColor == selectedColor }
    }

    private fun saveWishlist(items: List<ItemsModel>) {
        val jsonString = gson.toJson(items)
        sharedPreferences.edit().putString("wishlist", jsonString).apply()
    }

    private fun loadWishlist(): List<ItemsModel> {
        val jsonString = sharedPreferences.getString("wishlist", null)
        return jsonString?.let {
            val type = object : TypeToken<List<ItemsModel>>() {}.type
            gson.fromJson<List<ItemsModel>>(it, type) ?: emptyList()
        } ?: emptyList()
    }

    fun addItemToCart(item: ItemsModel) {
        val updatedCartList = _cartItems.value + item
        _cartItems.value = updatedCartList
        Log.d("CartViewModel", "Item added to cart. Cart size: ${_cartItems.value.size}")
        //TODO: Save Cart Items
    }
}