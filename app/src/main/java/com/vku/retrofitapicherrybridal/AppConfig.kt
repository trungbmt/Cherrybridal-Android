package com.vku.retrofitapicherrybridal

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import com.google.gson.Gson




object AppConfig {
    const val BASE_URL = "http://192.168.1.19/cherrybridal/"
    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    val retrofit = builder.build()
}