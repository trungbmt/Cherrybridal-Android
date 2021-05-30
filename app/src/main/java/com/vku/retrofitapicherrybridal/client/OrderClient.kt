package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface OrderClient {
    @GET("api/orders")
    fun orders(@HeaderMap headers: HashMap<String, String>) : Call<JsonObject>

    @POST("api/orders/abort/{id}")
    fun abortOrder(@HeaderMap headers: HashMap<String, String>, @Path("id") id : Int) : Call<JsonObject>
}