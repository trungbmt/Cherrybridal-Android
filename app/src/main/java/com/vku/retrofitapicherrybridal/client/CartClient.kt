package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query

interface CartClient {
    @POST("api/carts")
    fun storeCart(@HeaderMap headers : Map<String, String>, @Query("product_id") product_id : Int, @Query("detail_id") detail_id : Int, @Query("amount") amount : Int) : Call<JsonObject>
}