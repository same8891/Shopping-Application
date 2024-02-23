package com.example.personalproject2.data

import kotlinx.coroutines.flow.Flow


class CartRpository(
    private val cartDao: CartDao
){
    // Overriding interface methods with DAO operations
    fun getCart(user: String): Flow<List<CartEntity>> = cartDao.getCart(user)

    suspend fun insertCart(c: CartEntity) = cartDao.insert(c)

    suspend fun update(c: CartEntity) = cartDao.update(c)
    suspend fun delete(c: CartEntity) = cartDao.delete(c)

    suspend fun deleteCart(id: Int) = cartDao.deletecart(id)
    suspend fun cleanCart(user: String) = cartDao.cleancart(user)
    suspend fun updateCart(id: Int, num: String) = cartDao.updateCart(id = id,num = num)
}