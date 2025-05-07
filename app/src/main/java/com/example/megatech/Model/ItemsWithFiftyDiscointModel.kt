package com.example.megatech.Model

import com.google.gson.annotations.SerializedName

class ItemsWithFiftyDiscointModel (

    @SerializedName("_id") val idDiscointFifty: String,
    @SerializedName("imageUrl") val imageUrlDiscointFifty: String,
    @SerializedName("name") val nameDiscointFifty: String,
    @SerializedName("normalPrice") val normalPriceDiscointFifty: Double,
    @SerializedName("priceWithDiscount") val priceWithDiscointFifty: Double,


    )