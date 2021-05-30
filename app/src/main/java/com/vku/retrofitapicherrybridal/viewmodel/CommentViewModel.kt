package com.vku.retrofitapicherrybridal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.client.CommentClient
import com.vku.retrofitapicherrybridal.model.PostComment
import com.vku.retrofitapicherrybridal.model.PostCommentAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentViewModel : ViewModel() {
    private val commentClient = AppConfig.retrofit.create(CommentClient::class.java)
    var mCommentsLiveData = MutableLiveData<ArrayList<PostComment>>()
    var mNewComment = MutableLiveData<PostComment>()

    fun getComments(id : Int) {
        val commentService = commentClient.getComments(id)
        commentService.enqueue(object : Callback<PostCommentAPI> {
            override fun onFailure(call: Call<PostCommentAPI>, t: Throwable) {
                Log.d("COMMENT", "Fail ${t.message}")
            }

            override fun onResponse(call: Call<PostCommentAPI>, response: Response<PostCommentAPI>) {
                Log.d("COMMENT", "respon ${response.body()}")
                if(response.isSuccessful) {
                    var comments = response.body()?.comments
                    if(comments!=null && comments?.size>0 ) {
                        mCommentsLiveData.value = comments!!
                    }
                    else {
                        mCommentsLiveData.value = ArrayList<PostComment>()
                    }
                }
            }

        })
    }
    fun postComment(id: Int, options : HashMap<String, String>) {
        var headers = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        if(token!=null) {
            headers.put("Authorization", "Bearer " + token)
            val commentService = commentClient.postComments(headers, id, options)
            commentService.enqueue(object : Callback<PostComment>{
                override fun onFailure(call: Call<PostComment>, t: Throwable) {
                    Log.d("POSTCMT", "Fail ${t.message}")
                }

                override fun onResponse(call: Call<PostComment>, response: Response<PostComment>) {
                    Log.d("POSTCMT", "Response ${response.body()}")
                    if(response.isSuccessful) {
                        if(response.body()!=null) {
                            mNewComment.value = response.body()
                        }
                    }
                }

            })
        }
    }
}