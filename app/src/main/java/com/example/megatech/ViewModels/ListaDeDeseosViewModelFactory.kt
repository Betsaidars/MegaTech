package com.example.megatech.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListaDeDeseosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaDeDeseosViewModel::class.java)) {
            return ListaDeDeseosViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}