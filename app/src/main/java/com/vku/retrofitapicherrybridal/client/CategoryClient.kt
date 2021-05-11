package com.vku.retrofitapicherrybridal.client

import com.vku.retrofitapicherrybridal.model.Category
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryClient {
    @GET(value = "api/categories")
    fun getCategories(): Call<CategoryAPI>

    @GET(value = "api/categories/{id}")
    fun getCategory(@Path("id") id : Int): Call<Category>
}