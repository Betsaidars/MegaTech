package com.example.megatech.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.megatech.Model.ItemsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListaDeDeseosViewModel: ViewModel(){

    private val _wishlistItems = MutableStateFlow<List<ItemsModel>>(emptyList())
    val wishlistItems: StateFlow<List<ItemsModel>> = _wishlistItems

    fun addItemToWishlist(item: ItemsModel) {
        if (!_wishlistItems.value.contains(item)) {
            _wishlistItems.value = _wishlistItems.value + item
            Log.d("ListaDeDeseosViewModel", "Item agregado: ${item.name}, Lista actual: ${_wishlistItems.value.size}")
        }
    }

    fun removeItemFromWishlist(item: ItemsModel) {
        _wishlistItems.value = _wishlistItems.value.filter { it.id != item.id }
    }

    fun isItemInWishlist(itemId: String?): Boolean {
        return _wishlistItems.value.any { it.id == itemId }
    }

}