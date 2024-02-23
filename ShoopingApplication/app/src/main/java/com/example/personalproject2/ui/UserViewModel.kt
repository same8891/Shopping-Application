package com.example.personalproject2.ui

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
import com.example.personalproject2.data.LoginResponse
import com.example.personalproject2.data.RegisterResponse
import com.example.personalproject2.data.ShoppingRepository
import com.example.personalproject2.data.UserLogin
import com.example.personalproject2.data.UserRegis
import kotlinx.coroutines.launch

sealed interface RegisterState {
    data class Success(var regres: RegisterResponse): RegisterState
    object Error: RegisterState
    object Loading: RegisterState
}
sealed interface LoginState {
    data class Success(var loginres: LoginResponse): LoginState
    object Error: LoginState
    object Loading: LoginState
}

class UserviewModel (val shoppingRepository: ShoppingRepository): ViewModel(){
    var uiState: RegisterState by mutableStateOf(RegisterState.Loading)
        private set
    var LoginuiState: LoginState by mutableStateOf(LoginState.Loading)
        private set

    suspend fun PostLogin(username: String, password: String) {
            // Using a try/catch to handle possible Exception when updating state
            var data = UserLogin(username,password)
            LoginuiState = try {
                LoginState.Success(shoppingRepository.LoginAccount(data))
            } catch(e: retrofit2.HttpException) {
                LoginState.Error
            }
    }
    suspend fun PostRegister(username: String, email: String, password: String) {
        // Using a try/catch to handle possible Exception when updating state
        var data = UserRegis(username,email,password)
        uiState = try {
            RegisterState.Success(shoppingRepository.RegisterAccount(data))
        } catch(e: retrofit2.HttpException) {
            RegisterState.Error
        }

    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // Access the UsersRepository through the application's container
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShoppingApplication)
                val shoppingRepository = application.container.shoppingRepository
                UserviewModel(shoppingRepository = shoppingRepository)
            }
        }
    }
}