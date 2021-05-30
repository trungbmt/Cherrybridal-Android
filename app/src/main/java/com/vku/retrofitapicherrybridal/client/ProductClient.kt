package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.vku.retrofitapicherrybridal.model.Product
import com.vku.retrofitapicherrybridal.model.ProductAPI
import retrofit2.Call
import retrofit2.http.*

interface ProductClient {
    @GET(value = "api/products")
    fun getProducts(@QueryMap options: Map<String, String>): Call<ProductAPI>

    @GET(value = "api/products/{id}")
    fun getProduct(@Path("id") id : Int): Call<Product>

    @POST("api/rating")
    fun ratingProduct(@HeaderMap headers: Map<String,String>, @QueryMap options: Map<String, String>): Call<JsonPrimitive>
}