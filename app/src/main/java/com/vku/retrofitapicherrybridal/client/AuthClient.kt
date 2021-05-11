package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface AuthClient {
    @POST(value = "api/login")
    fun login(@Query("username") username: String, @Query("password") password: String) : Call<JsonObject>


    @POST(value = "api/register")
    fun register(
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password : String,
        @Query("password_confirmation") confirm : String
    ) : Call<JsonObject>


    @GET(value = "api/logout")
    fun logout(@HeaderMap headers : Map<String, String>) : Call<JsonObject>
}
