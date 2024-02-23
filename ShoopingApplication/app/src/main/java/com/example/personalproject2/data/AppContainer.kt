package com.example.personalproject2.data


import com.example.personalproject2.RestAPI.ShoppingApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val shoppingRepository: ShoppingRepository
}
class DefaultAppContainer: AppContainer {
    override val shoppingRepository: ShoppingRepository by lazy {
        ShoppingRepository(retrofitService)
    }


    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val baseUrl = "http://192.168.4.30:8080/"
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).client(client).build()


    //  Create a new Retrofit service by using the user API service
    private val retrofitService by lazy {
        retrofit.create(ShoppingApiService::class.java)
    }
}