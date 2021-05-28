package com.vku.retrofitapicherrybridal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.client.OrderClient
import com.vku.retrofitapicherrybridal.model.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderViewModel : ViewModel() {
    private val orderClient = AppConfig.retrofit.create(OrderClient::class.java)
    var mOrderLiveData = MutableLiveData<ArrayList<Order>>()

    fun getOrders() {

        var options = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        if(token!=null) {
            options.put("Authorization", "Bearer " + token)
        }
        var orderService = orderClient.orders(options)
        orderService.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("ORDER", "ERROR ${t.message}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("ORDER", "RESPON ${response.body()}")
                val turnsType = object : TypeToken<ArrayList<Order>>() {}.type
                mOrderLiveData.value = Gson().fromJson<ArrayList<Order>>(response.body()?.get("orders"), turnsType)
            }

        })
    }
}