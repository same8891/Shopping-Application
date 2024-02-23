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
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun register(context: Context, navController : NavHostController, userviewModel: UserviewModel) {
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ){
        val (loginLabel, accountLabel, passwordLabel, emailLabel, accountBox, passwordBox,emailBox, register, login) = createRefs()
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        Text(
            text = "Register",
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
                .padding(bottom = 100.dp, end = 190.dp)

        )

        Text(
            text = "Email:",
            fontSize = 20.sp,
            modifier = Modifier
                .constrainAs(emailLabel) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 20.dp, end = 200.dp)
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
                .padding(top = 60.dp, end = 200.dp)
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
            value = email,
            onValueChange = {email = it},
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .constrainAs(emailBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 10.dp, start = 100.dp).width(200.dp).height(50.dp)
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
                .padding(top = 90.dp, start = 100.dp).width(200.dp).height(50.dp)
        )

        Button(
            onClick = { navController.navigate(OrderScreen.Login.name) },
            modifier = Modifier
                .constrainAs(register) {
                    top.linkTo(passwordBox.bottom)
                    start.linkTo(passwordLabel.start)
                }
                .padding(start = 40.dp)
        ) {
            Text("Back")
        }

        Button(
            onClick = {
                userviewModel.viewModelScope.launch {
                    userviewModel.PostRegister(username, email, password)
                    var registerState = userviewModel.uiState
                    when (registerState) {
                        is RegisterState.Success -> {
                            Toast.makeText(context, "Register Successfull", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(OrderScreen.Login.name)
                        }
                        else -> {
                            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show()
                            Log.d("post1", "failed")
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
            Text("Regist")
        }
    }
}


