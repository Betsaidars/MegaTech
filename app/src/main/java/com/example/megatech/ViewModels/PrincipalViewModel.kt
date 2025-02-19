package com.example.megatech.ViewModels

import androidx.lifecycle.ViewModel
import com.example.megatech.Model.ItemsModel
import com.example.megatech.Repository.ProductosRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class PrincipalViewModel: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val repository = ProductosRepository()

    suspend fun getProductos(): List<ItemsModel>{
        return repository.getProductos()
    }

    fun signOut(){
        auth.signOut()
    }
}