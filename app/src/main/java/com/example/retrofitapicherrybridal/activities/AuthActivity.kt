package com.example.retrofitapicherrybridal.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.retrofitapicherrybridal.AppConfig
import com.example.retrofitapicherrybridal.MainApplication
import com.example.retrofitapicherrybridal.R
import com.example.retrofitapicherrybridal.client.AuthClient
import com.example.retrofitapicherrybridal.fragments.SignInFragment
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.prefs.PreferenceChangeListener

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().replace(R.id.frameAuthContainer, SignInFragment()).commit()

    }
    companion object {
        fun logout() {
            val userPref = MainApplication.userSharedPreferences()
            var editor = userPref.edit()
            editor.putBoolean("isLoggedIn", false)

            val authClient: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
            var headers = HashMap<String, String>()
            var token = userPref.getString("token", null)

            editor.remove("token")
            editor.apply()

            if(token!=null) {
                headers.put("Authorization", "Bearer " + token)
                val logoutService: Call<JsonObject> = authClient.logout(headers)
                logoutService.enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.d("TokenDestroy Failed", t.localizedMessage.toString())
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        Log.d("TokenDestroy Success", response.body().toString())
                    }

                })
                val context = MainApplication.applicationContext()
                context.startActivity(Intent(context, AuthActivity::class.java))
            }
        }
    }
}