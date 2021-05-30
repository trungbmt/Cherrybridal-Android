package com.vku.retrofitapicherrybridal.client

import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.model.PostComment
import com.vku.retrofitapicherrybridal.model.PostCommentAPI
import retrofit2.Call
import retrofit2.http.*

interface CommentClient {
    @GET("api/comments/post/{id}")
    fun getComments(@Path("id") id : Int) : Call<PostCommentAPI>

    @POST("api/comments/post/{id}")
    fun postComments(@HeaderMap headers : HashMap<String, String>, @Path("id") id : Int, @QueryMap options : HashMap<String, String>) : Call<PostComment>
}