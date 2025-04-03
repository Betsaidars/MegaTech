package com.example.megatech.Model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @SerializedName("id") val id: String,
    @SerializedName("username") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val pass: String

)
