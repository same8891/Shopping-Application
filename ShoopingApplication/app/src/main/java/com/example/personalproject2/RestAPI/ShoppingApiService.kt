package com.example.personalproject2.RestAPI

import com.example.personalproject2.data.LoginResponse
import com.example.personalproject2.data.OrderDetailResponse
import com.example.personalproject2.data.OrderResponse
import com.example.personalproject2.data.PostOrderRequest
import com.example.personalproject2.data.PostResponse
import com.example.personalproject2.data.ProductsResponse
import com.example.personalproject2.data.RegisterResponse
import com.example.personalproject2.data.UserLogin
import com.example.personalproject2.data.UserRegis
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ShoppingApiService {
    @POST("/users")
    suspend fun RegisterAccount(@Body data: UserRegis): RegisterResponse
    @POST("/users/login")
    suspend fun LoginAccount(@Body data: UserLogin): LoginResponse
    @POST("/orders")
    suspend fun SendOrder(@Header("Authorization") authToken: String, @Body data: PostOrderRequest): PostResponse
    @GET("/products")
    suspend fun GetProducts(@Header("Authorization") authToken: String): ProductsResponse
    @GET("/orders")
    suspend fun Getorders(@Header("Authorization") authToken: String): OrderResponse
    @GET("/orders/{id}")
    suspend fun Getorderdetail(@Header("Authorization") authToken: String,@Path("id") id: Int): OrderDetailResponse

}