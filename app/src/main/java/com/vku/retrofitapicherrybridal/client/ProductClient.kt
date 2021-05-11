package com.vku.retrofitapicherrybridal.client

import com.vku.retrofitapicherrybridal.model.Product
import com.vku.retrofitapicherrybridal.model.ProductAPI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductClient {
    @GET(value = "api/products")
    fun getProducts(): Call<ProductAPI>

    @GET(value = "api/products/{id}")
    fun getProduct(@Path("id") id : Int): Call<Product>
}