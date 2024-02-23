package com.example.personalproject2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.personalproject2.data.CartDatabase
import com.example.personalproject2.data.CartRpository
import com.example.personalproject2.ui.CartViewModel
import com.example.personalproject2.ui.UserviewModel
import com.example.personalproject2.ui.cartpage
import com.example.personalproject2.ui.login
import com.example.personalproject2.ui.order
import com.example.personalproject2.ui.orderdetail
import com.example.personalproject2.ui.productpage
import com.example.personalproject2.ui.register

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
//
                ShoppingApp(context = this)
            }

        }
    }
}
enum class OrderScreen(@StringRes val title: Int) {
    Login(title = R.string.Login),
    Regis(title = R.string.Regis),
    Product(title = R.string.Product),
    Cart(title = R.string.Cart),
    Orders(title = R.string.Orders),
    OrderDetail(title = R.string.OrderDetail)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp(context: Context, navController: NavHostController = rememberNavController())
{
    /*
        Retrieve the current backstack entry as state, allowing us to
        observe changes to the backstack entry and automatically update
        the UI whenever the user navigates between screens
    */
    val backStackEntry by navController.currentBackStackEntryAsState()
    val userViewModel: UserviewModel = viewModel(factory = UserviewModel.Factory)
    val cartRepository = CartRpository(CartDatabase.getDatabase(context).cartDao())
    val cartViewModel = CartViewModel(cartRepository)
    /*
        Extracting the name of the current screen from the back stack
        entry, or default to StartOrder if null
    */
    val currentScreen = OrderScreen.valueOf(
        backStackEntry?.destination?.route ?: OrderScreen.Login.name
    )

    Scaffold(
        topBar = {
            OrderAppTopBar(
                currentScreen = currentScreen,
                navigateUp = { navController.navigateUp() },
                canNavigateBack = navController.previousBackStackEntry != null)
        }
    ) {
        /*
            NavHost is the entry point of the NavGraph and
            handles navigation between screens
        */
        NavHost(
            navController = navController,
            // Starting Composable for the NavGraph
            startDestination = OrderScreen.Login.name,
            modifier = Modifier.padding(it)
        ) {
            /*
                Referencing a different constant in our enum class with
                the composable method to construct the NavGraph
            */
            composable(route = OrderScreen.Login.name) {
                login(context,navController = navController,userviewModel = userViewModel)
            }
            composable(route = OrderScreen.Regis.name) {
                register(context, navController = navController,userviewModel = userViewModel)
            }
            composable(route = OrderScreen.Product.name) {
                var auth : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("auth")
                var user : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("user")
//                if (auth != null) {
//                    Log.d("auth1",auth)
//                }
//                if (user != null) {
//                    Log.d("user1",user)
//                }
//                Log.d("pro","1")
                productpage(context,navController = navController,auth!!,user!!)
            }
            composable(route = OrderScreen.Cart.name) {
                var auth : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("auth")
                var user : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("user")
//                if (auth != null) {
//                    Log.d("auth2",auth)
//                }
//                if (user != null) {
//                    Log.d("user2",user)
//                }
                cartpage(context,navController = navController,auth!!,user!!,cartViewModel)
            }
            composable(route = OrderScreen.Orders.name) {
                var auth : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("auth")
                var user : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("user")
//                if (auth != null) {
//                    Log.d("auth2",auth)
//                }
//                if (user != null) {
//                    Log.d("user2",user)
//                }
                order(context,navController = navController,auth!!,user!!)
            }
            composable(route = OrderScreen.OrderDetail.name) {
                var auth : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("auth")
                var user : String? = navController.previousBackStackEntry?.savedStateHandle?.get<String?>("user")
                var id : Int? = navController.previousBackStackEntry?.savedStateHandle?.get<Int?>("productid")
                if (auth != null) {
                    Log.d("auth2",auth)
                }
                if (user != null) {
                    Log.d("user2",user)
                }
                orderdetail(context,navController = navController,auth!!,user!!,id!!)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderAppTopBar(
    currentScreen: OrderScreen,
    navigateUp: () -> Unit,
    canNavigateBack: Boolean
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = currentScreen.title))
        },
    )
}