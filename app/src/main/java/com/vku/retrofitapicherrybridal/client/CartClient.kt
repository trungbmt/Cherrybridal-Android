package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.model.Cart
import com.vku.retrofitapicherrybridal.model.CartAPI
import retrofit2.Call
import retrofit2.http.*

interface CartClient {
    @GET("api/carts")
    fun getCarts(@HeaderMap headers: Map<String, String>) : Call<CartAPI>

    @POST("api/carts/{id}")
    fun removeCart(@HeaderMap headers: Map<String, String>, @Path("id") id : Int) : Call<JsonObject>

    @POST("api/carts")
    fun storeCart(@HeaderMap headers : Map<String, String>, @Query("product_id") product_id : Int, @Query("detail_id") detail_id : Int, @Query("amount") amount : Int) : Call<JsonObject>

    @POST("api/order")
    fun order(@HeaderMap headers: Map<String, String>, @QueryMap queries: Map<String, String>) : Call<JsonObject>
}
