package com.example.retrofitapicherrybridal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.retrofitapicherrybridal.AppConfig
import com.example.retrofitapicherrybridal.R
import com.example.retrofitapicherrybridal.client.AuthClient
import com.example.retrofitapicherrybridal.fragments.SignInFragment
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().replace(R.id.frameAuthContainer, SignInFragment()).commit()
        logout()

    }
    private fun logout() {
        val authClient: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
        var headers = HashMap<String, String>()
        var token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3RcL2NoZXJyeWJyaWRhbFwvYXBpXC9sb2dpbiIsImlhdCI6MTYxODcxOTY5NiwiZXhwIjoxNjE4NzIzMjk2LCJuYmYiOjE2MTg3MTk2OTYsImp0aSI6IlgwNDFCenRJaXhnazZRNG4iLCJzdWIiOjE1LCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.YvHRNSlft8nbxgk8ly0xTAS1hel6HcnxalRFtiY_ooI"
        headers.put("Authorization", "Bearer " + token)
        val logoutService: Call<JsonObject> = authClient.logout(headers)
        logoutService.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("logoutresponse zxc", t.localizedMessage.toString())
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("logoutresponse", response.body().toString())
            }

        })
    }
}