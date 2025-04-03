package com.example.megatech.Model

import com.google.gson.annotations.SerializedName

data class BannerModel(

    @SerializedName("id")val id: String,
    @SerializedName("imageUrl")val imageUrl: String

)
