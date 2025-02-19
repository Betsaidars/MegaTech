package com.example.megatech.Model

data class ItemsModel(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String,
    val brand: String,
    val model: String,
    val avalaibleColors: List<String>,
    val imageUrl: List<String>,
    val price: Double,
    val rating: Double

)
