package com.vku.retrofitapicherrybridal.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.client.CategoryClient
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    private val categoryClient:CategoryClient = AppConfig.retrofit.create(CategoryClient::class.java)

    private val categoryService:Call<CategoryAPI> = categoryClient.getCategories()

    val userPref = MainApplication.userSharedPreferences()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)


        setData()
        getCategories()
        btnLogout.setOnClickListener{
            AuthActivity.logout()
            AuthActivity.logout_fb()
            AuthActivity.logout_gg(this)
            finish()
        }
//        loginToken()
    }

    private fun setData() {
        if(userPref.contains("username")) {
            tvName.text = userPref.getString("username", null)
        }
    }
    private fun getCategories() {
        categoryService.enqueue(object : Callback<CategoryAPI>{
            override fun onFailure(call: Call<CategoryAPI>, t: Throwable) {
                Log.d("response123", "Fail: "+t.message)
            }
            override fun onResponse(call: Call<CategoryAPI>, response: Response<CategoryAPI>) {
                Log.d("response123", "Success: "+response.body()!!.categories.get(0).toString())
            }

        })
    }


}