package com.example.tokobunga.repositori

import android.app.Application

class FloristApplication : Application() {

    lateinit var containerApp: ContainerApp
        private set

    override fun onCreate() {
        super.onCreate()
        containerApp = DefaultContainerApp()
    }
}
