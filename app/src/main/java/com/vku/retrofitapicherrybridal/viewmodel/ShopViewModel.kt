package com.vku.retrofitapicherrybridal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.client.CategoryClient
import com.vku.retrofitapicherrybridal.model.Category
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopViewModel : ViewModel() {

    private val categoryClient: CategoryClient = AppConfig.retrofit.create(CategoryClient::class.java)
    private val categoryService: Call<CategoryAPI> = categoryClient.getCategories()

    private var mCategoriesLiveData = MutableLiveData<ArrayList<Category>>()
    private var mCategories = ArrayList<Category>()

    init {
        getCategories()
    }
    fun getCategoriesLiveData() : MutableLiveData<ArrayList<Category>> {
        return this.mCategoriesLiveData
    }

    private fun getCategories() {
        categoryService.enqueue(object : Callback<CategoryAPI> {
            override fun onFailure(call: Call<CategoryAPI>, t: Throwable) {
                Log.d("CategoryAPI", "Fail: "+t.message)
            }
            override fun onResponse(call: Call<CategoryAPI>, response: Response<CategoryAPI>) {
                val categoryResponse = response.body()
                if(response.isSuccessful && categoryResponse!=null) {
                    mCategoriesLiveData.value = categoryResponse.categories
                }
            }

        })
    }
}