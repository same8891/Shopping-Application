package com.example.personalproject2.data

import com.example.personalproject2.RestAPI.ShoppingApiService
import okhttp3.ResponseBody
import retrofit2.Call

class ShoppingRepository(private val shoppingApiService : ShoppingApiService) {
    suspend fun RegisterAccount(data: UserRegis): RegisterResponse = shoppingApiService.RegisterAccount(data)
    suspend fun LoginAccount(data: UserLogin): LoginResponse = shoppingApiService.LoginAccount(data)
    suspend fun GetProducts(authToken: String): ProductsResponse = shoppingApiService.GetProducts(authToken)
    suspend fun GetOrders(authToken: String): OrderResponse = shoppingApiService.Getorders(authToken)
    suspend fun PostOrder(authToken: String, data: PostOrderRequest): PostResponse = shoppingApiService.SendOrder(authToken,data)
    suspend fun GetOrdersDetail(authToken: String, id:Int): OrderDetailResponse = shoppingApiService.Getorderdetail(authToken,id)

}