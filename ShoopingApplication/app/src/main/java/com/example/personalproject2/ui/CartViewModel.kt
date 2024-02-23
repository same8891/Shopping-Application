package com.example.personalproject2.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.personalproject2.ShoppingApplication
import com.example.personalproject2.data.CartDatabase
import com.example.personalproject2.data.CartEntity
import com.example.personalproject2.data.CartRpository
import com.example.personalproject2.data.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
sealed interface CartState {
    data class Success(var products: List<CartEntity>): CartState
    object Error: CartState
    object Loading: CartState
}
class CartViewModel(private val cartRepository: CartRpository) : ViewModel() {
    var uiState: CartState by mutableStateOf(CartState.Loading)
        private set
    suspend fun GetUserCart(context: Context, user: String): List<CartEntity> {
        var res :List<CartEntity> = emptyList()
//        uiState = CartState.Loading
        try {
            Log.d("getall", "get")
            cartRepository.getCart(user).collect { cartEntity ->
                res = cartEntity
                Log.d("get all","$res")
                uiState = CartState.Success(res)
                Log.d("Cartstate",uiState.toString())
            }
        } catch (e: Exception) {
            Log.d("error", "error get")
        }
        return res
    }
    suspend fun InsertCart(context: Context, addproduct: CartEntity): String {
        try {
            cartRepository.insertCart(addproduct)
            Log.d("insert", "insert successful")
        } catch (e: Exception) {
            Log.d("error", "error insert")
        }
        return "successful"
    }
    suspend fun DeleteCart(context: Context, user: String,deleteproduct: CartEntity): String {
        try {
            cartRepository.deleteCart(deleteproduct.id!!)
            Log.d("delete", "delete successful")
        } catch (e: Exception) {
            Log.d("error", "error insert")
        }
        return "successful"
    }
    suspend fun CleanCart(context: Context, user: String): String {
        try {
            cartRepository.cleanCart(user)
            Log.d("delete", "delete successful")
        } catch (e: Exception) {
            Log.d("error", "error insert")
        }
        return "successful"
    }
    suspend fun UpdateCart(context: Context, id: Int ,num: String): String {
        try {
            Log.d("updatecart","$id            $num")
            cartRepository.updateCart(id = id, num = num)
            Log.d("update", "update successful")
        } catch (e: Exception) {
            Log.d("error", "update error")
        }
        return "successful"
    }

}