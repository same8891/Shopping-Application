package com.example.personalproject2.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.personalproject2.OrderScreen
import com.example.personalproject2.data.CartDatabase
import com.example.personalproject2.data.CartEntity
import com.example.personalproject2.data.CartRpository
import com.example.personalproject2.data.Product
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun productpage(context: Context, navController : NavHostController, auth:String, user:String) {
    val productViewModel: ProductsViewModel = viewModel(factory = ProductsViewModel.Factory)
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    val cartViewModel = CartViewModel(cartRepository)
    LaunchedEffect(key1 = true) {
        productViewModel.GetProduct("Bearer $auth")
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
            ShoppingCartList(context,productViewModel.uiState,user,navController,auth)
        }
    }

}



@Composable
fun ShoppingCartList(context: Context, uiState: ProductsState,user:String,navController : NavHostController,auth:String) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
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
                navController.navigate(OrderScreen.Orders.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Orders")
        }
        Button(
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("auth", auth)
                navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                navController.navigate(OrderScreen.Cart.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Cart")
        }
        when (uiState) {
            is ProductsState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 60.dp)
                ) {
                    items(uiState.products.sortedBy { it.name }) { product ->
                        ShoppingCartItem(
                            context,
                            name = product.name,
                            price = product.retailPrice,
                            productid = product.id,
                            user = user
                        )
                    }
                }
            }

            is ProductsState.Loading -> {
                LoadingScreen()
            }

            is ProductsState.Error -> {
                ErrorScreen()
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartItem(context: Context,name: String, price : Double,user:String, productid : Int) {
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    val cartViewModel = CartViewModel(cartRepository)
    var isDropdownExpanded by remember { mutableStateOf(false) }
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
            Text(text = "price: $price", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (isOpen) {
                Row {
                    IconButton(onClick = {
                        if(num > 1)
                            num--
                        else if(num == 1)
                            Toast.makeText(context,"The min is 1", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                    }
                    Text(text = num.toString())
                    IconButton(onClick = {
                        if(num < 10)
                            num++
                        else if(num == 10)
                            Toast.makeText(context,"The max is 10", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                    Button(onClick = {
                        var cartEntity = CartEntity(user = user, product = name, num = num.toString(), price = price.toString(), productid = productid.toString())
                        cartViewModel.viewModelScope.launch {
                            cartViewModel.InsertCart(context,cartEntity)
                            Log.d("p",cartEntity.toString())
                            cartViewModel.GetUserCart(context,user)
                        }
                    }) {
                        Text(text = "add to cart")
                    }
                }
            }
        }
    }
}
@Composable
fun LoadingScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "Loading...")
    }
}

@Composable
fun ErrorScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Text(
            text = "404",
            fontSize = 40.sp,
            color = Color.Red,
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
        Text(
            text = "Whoops! Looks like something went wrong!",
            color = Color.Black
        )
    }
}

