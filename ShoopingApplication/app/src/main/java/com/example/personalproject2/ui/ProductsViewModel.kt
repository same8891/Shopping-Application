package com.example.personalproject2.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.personalproject2.ShoppingApplication
import com.example.personalproject2.data.LoginResponse
import com.example.personalproject2.data.Product
import com.example.personalproject2.data.ProductsResponse
import com.example.personalproject2.data.RegisterResponse
import com.example.personalproject2.data.ShoppingRepository
import com.example.personalproject2.data.UserLogin
import com.example.personalproject2.data.UserRegis
import kotlinx.coroutines.launch


sealed interface ProductsState {
    data class Success(var products: List<Product>): ProductsState
    object Error: ProductsState
    object Loading: ProductsState
}

class ProductsViewModel (val shoppingRepository: ShoppingRepository): ViewModel(){
    var uiState: ProductsState by mutableStateOf(ProductsState.Loading)
        private set
//    var Items : MutableLiveData<List<Product>> = MutableLiveData()
//    val mutableLiveData: LiveData<List<Product>> get() = _mutableLiveData
    fun GetProduct(auth: String) {
        viewModelScope.launch {
            Log.d("getproduct","get")
            // Using a try/catch to handle possible Exception when updating state
            uiState = try {
                ProductsState.Success(shoppingRepository.GetProducts(auth).products)
            } catch(e: retrofit2.HttpException) {
                ProductsState.Error
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Access the UsersRepository through the application's container
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShoppingApplication)
                val shoppingRepository = application.container.shoppingRepository
                ProductsViewModel(shoppingRepository = shoppingRepository)
            }
        }
    }
}