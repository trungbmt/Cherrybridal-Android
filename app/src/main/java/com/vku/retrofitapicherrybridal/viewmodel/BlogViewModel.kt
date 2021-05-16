package com.vku.retrofitapicherrybridal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.client.PostClient
import com.vku.retrofitapicherrybridal.model.Category
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import com.vku.retrofitapicherrybridal.model.Post
import com.vku.retrofitapicherrybridal.model.PostAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogViewModel : ViewModel() {

    private val postClient: PostClient = AppConfig.retrofit.create(PostClient::class.java)
    private val postService: Call<PostAPI> = postClient.getPosts()

    private var mPostsLiveData = MutableLiveData<ArrayList<Post>>()
    private var mPosts = ArrayList<Post>()
    init {
        getPosts()
    }
    fun getPostsLiveData() : MutableLiveData<ArrayList<Post>> {
        return this.mPostsLiveData
    }
    private fun getPosts() {
        postService.enqueue(object : Callback<PostAPI> {
            override fun onFailure(call: Call<PostAPI>, t: Throwable) {
                Log.d("ResponsePOST", "Fail: "+t.message)
            }
            override fun onResponse(call: Call<PostAPI>, response: Response<PostAPI>) {
                val postResponse = response.body()
                Log.d("ResponsePOST", ""+postResponse?.posts?.get(0).toString())
                if(response.isSuccessful && postResponse!=null) {
                    mPostsLiveData.value = postResponse.posts
                }
            }

        })
    }
}