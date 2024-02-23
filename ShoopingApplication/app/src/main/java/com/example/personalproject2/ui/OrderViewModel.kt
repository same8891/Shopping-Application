package com.example.personalproject2.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.personalproject2.ShoppingApplication
import com.example.personalproject2.data.Order
import com.example.personalproject2.data.Orderitem
import com.example.personalproject2.data.Orderitemdetail
import com.example.personalproject2.data.PostOrderRequest
import com.example.personalproject2.data.PostResponse
import com.example.personalproject2.data.Product
import com.example.personalproject2.data.ShoppingRepository
import kotlinx.coroutines.launch

sealed interface OrderState {
    data class Success(var orders: List<Orderitem>): OrderState
    object Error: OrderState
    object Loading: OrderState
}
sealed interface PostState {
    data class Success(var response: PostResponse): PostState
    object Error: PostState
    object Loading: PostState
}
sealed interface OrderDetailState {
    data class Success(var orderdetails: List<Orderitemdetail>): OrderDetailState
    object Error: OrderDetailState
    object Loading: OrderDetailState
}
class OrderViewModel (val shoppingRepository: ShoppingRepository): ViewModel(){
    var uiState: OrderState by mutableStateOf(OrderState.Loading)
        private set
    var postuiState: PostState by mutableStateOf(PostState.Loading)
        private set
    var detailState: OrderDetailState by mutableStateOf(OrderDetailState.Loading)
        private set
    //    var Items : MutableLiveData<List<Product>> = MutableLiveData()
//    val mutableLiveData: LiveData<List<Product>> get() = _mutableLiveData
    fun GetOrders(auth: String) {
        viewModelScope.launch {
            Log.d("getproduct","get")
            // Using a try/catch to handle possible Exception when updating state
            uiState = try {
                OrderState.Success(shoppingRepository.GetOrders(auth).orders)
            } catch(e: retrofit2.HttpException) {
                OrderState.Error
            }
            Log.d("order ui",uiState.toString())
        }
    }
    fun GetOrderDetail(auth: String,id : Int) {
        viewModelScope.launch {
            Log.d("getproduct","get")
            // Using a try/catch to handle possible Exception when updating state
            detailState = try {
                OrderDetailState.Success(shoppingRepository.GetOrdersDetail(auth,id).order.orderItems)
            } catch(e: retrofit2.HttpException) {
                OrderDetailState.Error
            }
            Log.d("order ui",detailState.toString())
        }
    }
    suspend fun SendOrder(auth: String,orders: List<Order>){
        val postrequest = PostOrderRequest(orders)
        postuiState = try {
            PostState.Success(shoppingRepository.PostOrder(auth,postrequest))
        } catch(e: retrofit2.HttpException) {
            PostState.Error
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Access the UsersRepository through the application's container
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShoppingApplication)
                val shoppingRepository = application.container.shoppingRepository
                OrderViewModel(shoppingRepository = shoppingRepository)
            }
        }
    }
}