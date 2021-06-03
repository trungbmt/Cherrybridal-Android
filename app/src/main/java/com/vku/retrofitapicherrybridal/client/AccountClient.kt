package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AccountClient {

    @Multipart
    @POST("api/profile/avatar")
    fun changeAvatar(@HeaderMap headers : HashMap<String, String>, @Part avatar: MultipartBody.Part) : Call<JsonObject>
}