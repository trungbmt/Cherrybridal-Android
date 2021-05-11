package com.vku.retrofitapicherrybridal

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MainApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
        fun userSharedPreferences() : SharedPreferences {

            val userPref = applicationContext().getSharedPreferences("user", Context.MODE_PRIVATE)
            return userPref
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MainApplication.applicationContext()
    }
}