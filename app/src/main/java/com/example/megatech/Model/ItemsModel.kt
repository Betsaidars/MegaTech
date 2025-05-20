package com.example.megatech.Model

import com.google.gson.annotations.SerializedName

data class ItemsModel(

    @SerializedName("id") val id: String,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("model") val model: String,
    @SerializedName("availableColors") val availableColors: List<String?>?,
    @SerializedName("imageUrl") val imageUrl: List<String>,
    @SerializedName("price") val price: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("cantidad") val cantidad: Int = 1,
    var selectedColor: String? = null

)
