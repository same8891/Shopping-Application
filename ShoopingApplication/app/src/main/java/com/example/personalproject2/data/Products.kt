package com.example.personalproject2.data

data class Product(var id: Int, var name:String,var description: String, var retailPrice: Double)
data class ProductsResponse(var status: Status, var products : List<Product>)