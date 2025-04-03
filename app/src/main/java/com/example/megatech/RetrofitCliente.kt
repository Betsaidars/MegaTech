package com.example.megatech

import android.annotation.SuppressLint
import android.content.Context
import com.example.megatech.Interface.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("StaticFieldLeak")
object RetrofitCliente {

    private lateinit var context: Context
    private lateinit var sessionManager: SessionManager


    private const val BASE_URL = "https://apimegatech3-production.up.railway.app"

    fun init(context: Context) {
        this.context = context.applicationContext
        sessionManager = SessionManager(context)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}