package com.example.megatech.Model

import com.google.gson.annotations.SerializedName

class ItemsWithDiscountModel (

    @SerializedName("_id") val idDiscoint: String,
    @SerializedName("imageUrl") val imageUrlDiscoint: String,
    @SerializedName("name") val nameDiscoint: String,
    @SerializedName("description") val descriptionDiscoint: String,
    @SerializedName("normalPrice") val normalPriceDiscoint: Double,
    @SerializedName("priceWithDiscoint") val priceWithDiscoint: Double,

)
