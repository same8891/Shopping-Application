package com.example.personalproject2.data

import java.util.Date

data class Order(val productId: Int, val quantity:Int)
data class PostOrderRequest(val order: List<Order>)
data class PostResponse(val success: Boolean, val message: String)
data class Orderitem(val orderId: Int, val userId: Int, val username: String, val orderStatus : String, val dataPLaced: Date)
data class OrderResponse(val status: Status,val orders: List<Orderitem>)
data class OrderDetailResponse(val status: Status,val order: Orderdetail)
data class Orderdetail(val orderId: Int, val orderStatus : String,val dataPLaced: Date, val orderItems:List<Orderitemdetail>)
data class Orderitemdetail(val id: Int, val product: Product, val purchasedPrice: Double, val quantity : Int)