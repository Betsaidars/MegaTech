package com.example.megatech.Model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @SerializedName("id") val id: String? = null,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String

)
