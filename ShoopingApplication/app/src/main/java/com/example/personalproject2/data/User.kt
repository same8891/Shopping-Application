package com.example.personalproject2.data

data class UserRegis (var username: String,var email: String,var password: String )
data class UserLogin (var username: String,var password: String )
data class RegisterResponse(val success: Boolean, val message: String)
data class Status(val success: Boolean, val message: String)
data class LoginResponse(val status: Status, val token: String)