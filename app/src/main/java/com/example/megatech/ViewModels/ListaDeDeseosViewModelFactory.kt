package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.megatech.SessionManager

class ListaDeDeseosViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListaDeDeseosViewModel::class.java)) {
            return ListaDeDeseosViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}