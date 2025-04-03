package com.example.megatech.Interface

import com.example.megatech.Model.BannerModel
import com.example.megatech.Model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("register")
    suspend fun createUser(@Body user: UserModel): Response<UserModel>

    @POST("api/users/login")
    suspend fun login(@Body user: UserModel): Response<Map<String, String>>

    @GET("/api/banners")
    suspend fun getAllBanner(): Response<List<BannerModel>>





}
