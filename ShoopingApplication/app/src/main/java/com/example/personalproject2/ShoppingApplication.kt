package com.example.personalproject2

import android.app.Application
import com.example.personalproject2.data.AppContainer
import com.example.personalproject2.data.DefaultAppContainer

class ShoppingApplication: Application() {
    // App container for managing dependencies
    lateinit var container: AppContainer

    // Called when the app is starting
    override fun onCreate() {
        super.onCreate()
        // Initialize the app container
        container = DefaultAppContainer()
    }
}