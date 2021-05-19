package com.vku.retrofitapicherrybridal.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.client.ProductClient
import com.vku.retrofitapicherrybridal.model.Product
import com.vku.retrofitapicherrybridal.model.ProductAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {

    private val productClient: ProductClient = AppConfig.retrofit.create(ProductClient::class.java)

    private var mProductsLiveData = MutableLiveData<ArrayList<Product>>()
    private var mProducts = ArrayList<Product>()

    private var mProductLiveData = MutableLiveData<Product>()
    private var mProduct = Product()

    fun getProductsLiveData() : MutableLiveData<ArrayList<Product>> {
        return this.mProductsLiveData
    }
    fun getProductLiveData() : MutableLiveData<Product> {
        return this.mProductLiveData
    }
    fun getProduct(product_id : Int) {
        val productService = productClient.getProduct(product_id)
        productService.enqueue(object : Callback<Product>{
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.d("ResponseProduct", "Fail: "+t.message)
            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                val productResponse = response.body()
                Log.d("ResponseProduct", ""+productResponse)
                if(response.isSuccessful && productResponse!=null) {
                    mProductLiveData.value = productResponse!!
                }
            }

        })
    }
    private fun getProducts() {
        val productService = productClient.getProducts()
        productService.enqueue(object : Callback<ProductAPI> {
            override fun onFailure(call: Call<ProductAPI>, t: Throwable) {
                Log.d("ResponseProduct", "Fail: "+t.message)
            }
            override fun onResponse(call: Call<ProductAPI>, response: Response<ProductAPI>) {
                val productResponse = response.body()
                Log.d("ResponseProduct", ""+productResponse)
                if(response.isSuccessful && productResponse!=null) {
                    mProductsLiveData.value = productResponse.products
                }
            }

        })
    }
}