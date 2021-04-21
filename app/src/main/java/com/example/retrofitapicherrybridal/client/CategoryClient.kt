package com.example.retrofitapicherrybridal.client

import com.example.retrofitapicherrybridal.model.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryClient {
    @GET(value = "api/categories")
    fun getCategories(): Call<List<Category>>

    @GET(value = "api/categories/{id}")
    fun getCategory(@Path("id") id : Int): Call<Category>
}