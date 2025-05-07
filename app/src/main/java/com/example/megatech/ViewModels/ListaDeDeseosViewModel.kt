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
        if (!_wishlistItems.value.none { it.id == item.id }) return
        val updatedList = _wishlistItems.value + item
        _wishlistItems.value = updatedList // Esto emite el nuevo estado
        Log.d("WishlistFlow", "Emitiendo nueva lista al agregar: ${updatedList.size} items")
    }

    fun removeItemFromWishlist(item: ItemsModel) {
        val updatedList = _wishlistItems.value.filter { it.id != item.id }
        _wishlistItems.value = updatedList // Esto emite el nuevo estado
        Log.d("WishlistFlow", "Emitiendo nueva lista al eliminar: ${updatedList.size} items")
    }

    fun isItemInWishlist(itemId: String?): Boolean {
        return _wishlistItems.value.any { it.id == itemId }
    }

    private fun saveWishlist(items: List<ItemsModel>) {
        val jsonString = gson.toJson(items)
        sharedPreferences.edit().putString("wishlist", jsonString).apply()
    }

    private fun loadWishlist(): List<ItemsModel> {
        val jsonString = sharedPreferences.getString("wishlist", null)
        return jsonString?.let {
            val type = object : TypeToken<List<ItemsModel>>() {}.type
            gson.fromJson<List<ItemsModel>>(it, type) // <--- Aquí ocurre el NullPointerException
        } ?: emptyList()
    }

    fun addItemToCart(item: ItemsModel) {
        val updatedCartList = _cartItems.value + item
        _cartItems.value = updatedCartList
        Log.d("CartViewModel", "Item added to cart. Cart size: ${_cartItems.value.size}")
        //TODO: Save Cart Items
    }


}