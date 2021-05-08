package com.example.retrofitapicherrybridal.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.example.retrofitapicherrybridal.AppConfig
import com.example.retrofitapicherrybridal.MainApplication
import com.example.retrofitapicherrybridal.R
import com.example.retrofitapicherrybridal.client.CategoryClient
import com.example.retrofitapicherrybridal.model.Category
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val categoryClient:CategoryClient = AppConfig.retrofit.create(CategoryClient::class.java)

    private val categoryService:Call<Category> = categoryClient.getCategory(1)

    val userPref = MainApplication.userSharedPreferences()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        if(!userPref.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        setData()
        btnLogout.setOnClickListener{
            AuthActivity.logout()
            finish()
        }

//        loginToken()
    }

    private fun setData() {
        if(userPref.contains("username")) {
            tvName.text = userPref.getString("username", null)
        }
    }
    private fun fetchData() {
        categoryService.enqueue(object : Callback<Category>{

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("response123", "Fail: "+t.message)
            }

            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                Log.d("response123", "Success: "+response.body().toString())
            }

        })
    }
}