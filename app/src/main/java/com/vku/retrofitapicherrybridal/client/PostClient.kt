package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import com.vku.retrofitapicherrybridal.model.PostAPI
import com.vku.retrofitapicherrybridal.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface PostClient {

    @GET(value = "api/posts")
    fun getPosts(@HeaderMap headers : Map<String, String>): Call<PostAPI>

    @Multipart
    @POST("api/posts")
    fun createPost(@HeaderMap headers : Map<String, String>, @Part media: MultipartBody.Part, @Part("description") description: String): Call<JsonObject>

    @POST("api/posts/like")
    fun likePost(@HeaderMap headers: Map<String, String>, @Query("post_id") post_id: Int): Call<JsonObject>
}