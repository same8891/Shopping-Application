package com.example.personalproject2.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.personalproject2.OrderScreen
import com.example.personalproject2.data.CartDatabase
import com.example.personalproject2.data.CartEntity
import com.example.personalproject2.data.CartRpository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun order(context: Context, navController : NavHostController, auth:String, user:String) {
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModel.Factory)
    LaunchedEffect(key1 = true) {
        orderViewModel.GetOrders("Bearer $auth")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OrderList(context,orderViewModel.uiState,user,navController,auth)
        }
    }

}



@Composable
fun OrderList(context: Context, uiState: OrderState, user:String, navController : NavHostController, auth:String) {
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("auth", auth)
                navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                navController.navigate(OrderScreen.Product.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Back")
        }
        when (uiState) {
            is OrderState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 60.dp)
                ) {
                    items(uiState.orders.sortedBy { it.orderId }) { order ->
                        OrderItem(
                            context,
                            id = order.orderId,
                            status = order.orderStatus,
                            auth = auth,
                            navController,
                            user
                        )
                    }
                }
            }

            is OrderState.Loading -> {
                LoadingScreen()
            }

            is OrderState.Error -> {
                ErrorScreen()
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderItem(context: Context, id: Int, status : String,auth: String,navController : NavHostController,user:String) {
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    val cartViewModel = CartViewModel(cartRepository)
    var num by remember {
        mutableStateOf(1)
    }
    var isOpen by remember {
        mutableStateOf(false)
    }
    Card(
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("auth", auth)
            navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
            navController.currentBackStackEntry?.savedStateHandle?.set("productid", id)
            navController.navigate(OrderScreen.OrderDetail.name)
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = " orderid $id", style = MaterialTheme.typography.headlineLarge)
            Text(text = "orderStatus: $status", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}
