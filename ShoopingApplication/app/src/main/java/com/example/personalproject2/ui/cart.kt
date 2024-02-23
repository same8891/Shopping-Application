package com.example.personalproject2.ui

import android.annotation.SuppressLint
import android.app.PictureInPictureUiState
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.personalproject2.OrderScreen
import com.example.personalproject2.data.CartDatabase
import com.example.personalproject2.data.CartEntity
import com.example.personalproject2.data.CartRpository
import com.example.personalproject2.data.Order
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun cartpage(context: Context, navController : NavHostController, auth:String, user:String,cartViewModel : CartViewModel) {
    val orderViewModel: OrderViewModel = viewModel(factory = OrderViewModel.Factory)
    LaunchedEffect(Unit) {
        // Initialization code here
        cartViewModel.GetUserCart(context,user)
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
            CartList(context,cartViewModel.uiState,user,orderViewModel,auth,navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}



@Composable
fun CartList(context: Context, uiState: CartState, user:String,orderViewModel: OrderViewModel,auth:String,navController : NavHostController) {
    Log.d("c",uiState.toString())
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    val cartViewModel = CartViewModel(cartRepository)
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
        Button(
            onClick = {
                when (uiState) {
                    is CartState.Success -> {
                        var orderrequest : MutableList<Order> = mutableListOf()
                        for(p in uiState.products){
                            orderrequest.add(Order(p.productid!!.toInt(),p.num!!.toInt()))
                        }
                        Log.d("order",orderrequest.toString())
                        if(orderrequest.size!=0)
                        {
                        orderViewModel.viewModelScope.launch{
                            val mes = orderViewModel.SendOrder("Bearer $auth",orderrequest.toList())
                            var post = orderViewModel.postuiState
                            when(post){
                                is PostState.Success ->{
                                    if(post.response.success){
                                        Toast.makeText(context,"Order Successful",Toast.LENGTH_SHORT).show()
                                        cartViewModel.CleanCart(context,user)
                                        navController.currentBackStackEntry?.savedStateHandle?.set("auth", auth)
                                        navController.currentBackStackEntry?.savedStateHandle?.set("user", user)
                                        navController.navigate(OrderScreen.Product.name)
                                    }
                                }
                                else ->{
                                    Toast.makeText(context,"Not enough inventory for selected products. Order canceled",Toast.LENGTH_SHORT).show()

                                }
                            }
                        }
                    }
                        else{
                            Toast.makeText(context,"The cart is empty",Toast.LENGTH_SHORT).show()
                        }
                    }
                    is CartState.Loading -> {
                        Log.d("confirm","loading")
                    }

                    is CartState.Error -> {
                        Log.d("confirm","error")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Confirm")
        }
        when (uiState) {
            is CartState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 60.dp)
                ) {
                    items(uiState.products.sortedBy { it.product }) { product ->
                        CartItem(
                            context,
                            id = product.id!!,
                            name = product.product!!,
                            price = product.price!!,
                            user = user,
                            num = product.num!!.toInt()
                        )
                    }
                }
            }

            is CartState.Loading -> {
                LoadingScreen()
            }

            is CartState.Error -> {
                ErrorScreen()
            }
        }
    }

}



@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItem(context: Context, id:Int,name: String, price: String, user:String, num: Int) {
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    var cartListChanged by remember { mutableStateOf(true) }
    val cartViewModel = CartViewModel(cartRepository)
    val original_num = num
    var num_tmp by mutableStateOf(num)
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
                        if(num_tmp > 1)
                            num_tmp--
                        else if(num_tmp == 1)
                            Toast.makeText(context,"The min is 1",Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = null)
                    }
                    Text(text = num_tmp.toString())
                    IconButton(onClick = {
                        if(num_tmp < 10)
                            num_tmp++
                        else if(num_tmp == 10)
                            Toast.makeText(context,"The max is 10",Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                    Button(onClick = {
                        var cartEntity = CartEntity(user = user, product = name, num = num.toString(), price = price.toString())
                        cartViewModel.viewModelScope.launch {
//                            cartViewModel.InsertCart(context,cartEntity)
                            Log.d("update","$id    $num_tmp")
                            cartViewModel.UpdateCart(context,id,num_tmp.toString())
                            cartListChanged = !cartListChanged
                        }
                    }) {
                        Text(text = "save")
                    }
                    IconButton(onClick = {
                        var cartEntity = CartEntity(id = id,user = user, product = name, num = original_num.toString(), price = price.toString())
                        cartViewModel.viewModelScope.launch {
                            cartViewModel.DeleteCart(context,user, cartEntity)
                            cartListChanged = !cartListChanged
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}


