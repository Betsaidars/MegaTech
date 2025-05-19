package com.example.megatech.Interface

import com.example.megatech.Model.BannerModel
import com.example.megatech.Model.ItemsModel
import com.example.megatech.Model.ItemsWithDiscountModel
import com.example.megatech.Model.ItemsWithFiftyDiscointModel
import com.example.megatech.Model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    // USERS

    @POST("register")
    suspend fun createUser(@Body user: UserModel): Response<UserModel>

    @POST("api/users/login")
    suspend fun login(@Body user: UserModel): Response<Map<String, String>>

    @POST("api/users/register")
    suspend fun registerUser(@Body userModel: UserModel): Response<Map<String, Any>>

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body updatedUser: UserModel): Response<UserModel>

    @PUT("/api/users/{id}/password")
    suspend fun changePassword(@Path("id") id: String, @Body passwordUpdate: Map<String, String>): Response<Void>


    // BANNERS

    @GET("/api/banners")
    suspend fun getAllBanner(): Response<List<BannerModel>>


    @GET("/api/items")
    suspend fun getAllItems(): Response<List<ItemsModel>>

    // ITEMS

    @GET
    suspend fun getItemById(): Response<List<ItemsModel>>

    // ITEMS EN BANNER DEL 30%

    @GET("/api/items/discounted")
    suspend fun getDiscountedItems(): Response<List<ItemsWithDiscountModel>>

    @GET("/api/items/discountedFifty")
    suspend fun getDiscountedFiftyItems(): Response<List<ItemsWithFiftyDiscointModel>>

}
