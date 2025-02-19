package com.example.megatech.Repository

import com.example.megatech.Model.ItemsModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class ProductosRepository {

    private val database = FirebaseDatabase.getInstance()
    private val productosRef = database.getReference("productos")

    suspend fun getProductos(): List<ItemsModel>{
        return try {
            val snapshot = productosRef.get().await()
            val productos = mutableListOf<ItemsModel>()
            for (child in snapshot.children){
                val producto = child.getValue(ItemsModel::class.java)
                producto?.let { productos.add(it) }
            }
            productos
        }catch (e: Exception){
            println("Error al obtener los productos: ${e.message}")
            emptyList()
        }
    }
}