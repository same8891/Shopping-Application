package com.example.personalproject2.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.personalproject2.OrderScreen
import com.example.personalproject2.data.CartDatabase
import com.example.personalproject2.data.CartRpository

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun orderdetail(context: Context, navController : NavHostController, auth:String, user:String, orderid:Int) {
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModel.Factory)
    LaunchedEffect(key1 = true) {
        orderViewModel.GetOrderDetail("Bearer $auth",orderid)
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
            OrderDetailList(context,orderViewModel.detailState,user,navController,auth)
        }
    }

}



@Composable
fun OrderDetailList(context: Context, detailState: OrderDetailState, user:String, navController : NavHostController, auth:String) {
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
                navController.currentBackStackEntry?.savedStateHandle?.set("productid", 1)
                navController.navigate(OrderScreen.Orders.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Back")
        }
        when (detailState) {
            is OrderDetailState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 60.dp)
                ) {
                    items(detailState.orderdetails.sortedBy { it.product.name }) { orderdetail ->
                        OrderDetailItem(
                            context,
                            name = orderdetail.product.name,
                            price = orderdetail.purchasedPrice,
                            quantity = orderdetail.quantity,
                            auth = auth
                        )
                    }
                }
            }

            is OrderDetailState.Loading -> {
                LoadingScreen()
            }

            is OrderDetailState.Error -> {
                ErrorScreen()
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailItem(context: Context, name: String, price: Double, quantity: Int, auth: String) {
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    val cartViewModel = CartViewModel(cartRepository)
    var num by remember {
        mutableStateOf(1)
    }
    var isOpen by remember {
        mutableStateOf(false)
    }
    Card(
        onClick = { isOpen = !isOpen },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.headlineLarge)
            Text(text = "Price: $price", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Quantity: $quantity", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}