package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface OrderClient {
    @GET("api/orders")
    fun orders(@HeaderMap headers: HashMap<String, String>) : Call<JsonObject>
}