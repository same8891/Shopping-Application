package com.example.personalproject2.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.personalproject2.OrderScreen
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login(context: Context, navController : NavHostController, userviewModel: UserviewModel) {
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ){
        val (loginLabel, accountLabel, passwordLabel, accountBox, passwordBox, register, login) = createRefs()
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Text(
            text = "Login",
            fontSize = 50.sp,
            modifier = Modifier
                .constrainAs(loginLabel) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(bottom = 300.dp)
        )

        Text(
            text = "Account:",
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(accountLabel) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 110.dp, end = 190.dp)

        )

        Text(
            text = "Password:",
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(passwordLabel) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 10.dp, end = 200.dp)
        )

        TextField(
            value = username,
            onValueChange = {username = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .constrainAs(accountBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 110.dp, start = 100.dp).width(200.dp).height(50.dp)
        )
        TextField(
            value = password,
            onValueChange = {password = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .constrainAs(passwordBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 10.dp, start = 100.dp).width(200.dp).height(50.dp)
        )

        Button(
            onClick = { navController.navigate(OrderScreen.Regis.name) },
            modifier = Modifier
                .constrainAs(register) {
                    top.linkTo(passwordBox.bottom)
                    start.linkTo(passwordLabel.start)
                }
                .padding(start = 40.dp)
        ) {
            Text("Register")
        }

        Button(
            onClick = {
                userviewModel.viewModelScope.launch {
                    userviewModel.PostLogin(username, password)

                    var s = userviewModel.LoginuiState
                    when (s) {
                        is LoginState.Success -> {
                            if (s.loginres.status.success) {
                                Toast.makeText(context, "Login Successfull", Toast.LENGTH_SHORT)
                                    .show()
                                navController.currentBackStackEntry?.savedStateHandle?.set("auth", s.loginres.token)
                                navController.currentBackStackEntry?.savedStateHandle?.set("user", username)
                                navController.navigate(OrderScreen.Product.name)
                            }
                        }

                        else -> {
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                            Log.d("login1", "failed")
                        }
                    }
                }
            },
            modifier = Modifier
                .constrainAs(login) {
                    top.linkTo(passwordBox.bottom)
                    start.linkTo(register.end)
                }
                .padding(start = 25.dp)
        ) {
            Text("Login")
        }
    }
}


