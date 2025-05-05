package com.example.megatech.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CarritoDeCompraViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoDeCompraViewModel::class.java)) {
            return CarritoDeCompraViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}