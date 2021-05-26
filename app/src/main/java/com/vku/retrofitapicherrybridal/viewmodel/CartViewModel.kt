package com.vku.retrofitapicherrybridal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.client.CartClient
import com.vku.retrofitapicherrybridal.model.Cart
import com.vku.retrofitapicherrybridal.model.CartAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {

    private val cartClient = AppConfig.retrofit.create(CartClient::class.java)
    var mCartsLiveData = MutableLiveData<ArrayList<Cart>>()
    var mCostLiveData = MutableLiveData<Int>()

    fun getCarts() {
        var options = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        if(token!=null) {
            options.put("Authorization", "Bearer " + token)
        }
        val cartService = cartClient.getCarts(options)
        cartService.enqueue(object : Callback<CartAPI>{
            override fun onFailure(call: Call<CartAPI>, t: Throwable) {
                Log.d("GETCART", "ERROR ${t.message}")
            }

            override fun onResponse(call: Call<CartAPI>, response: Response<CartAPI>) {
                Log.d("GETCART", "RESPON "+response.body())
                if(response.isSuccessful) {
                    mCartsLiveData.value = response.body()?.carts
                    mCostLiveData.value = response.body()?.totalCost
                }
            }

        })
    }
    fun removeCart(id : Int) {
        var options = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        if(token!=null) {
            Log.d("CARTRMV", "TOKEN SET/ REMOVE CART ID $id")
            options.put("Authorization", "Bearer " + token)
            val cartService = cartClient.removeCart(options, id)
            cartService.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("CARTRMV", "ERROR ${t.message}")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("CARTRMV", "SUCCESS ${response.body().toString()}")
                    if(response.isSuccessful) {
                        mCostLiveData.value = response.body()?.get("cost")?.asInt
                    }
                }

            })
        } else {
            Log.d("CARTRMV", "TOKEN NULL")
        }
    }
}