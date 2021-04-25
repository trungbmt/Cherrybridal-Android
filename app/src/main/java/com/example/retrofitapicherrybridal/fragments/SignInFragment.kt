package com.example.retrofitapicherrybridal.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.retrofitapicherrybridal.AppConfig
import com.example.retrofitapicherrybridal.MainApplication
import com.example.retrofitapicherrybridal.R
import com.example.retrofitapicherrybridal.activities.MainActivity
import com.example.retrofitapicherrybridal.client.AuthClient
import com.example.retrofitapicherrybridal.model.User
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInFragment : Fragment() {
    lateinit var rootView : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_sign_in, container,false)
        rootView.txtSignUp.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.frameAuthContainer, SignUpFragment()).commit()
        }
        rootView.txtUsername.doOnTextChanged { text, start, before, count ->
            if(!txtUsername.text.toString().isEmpty()) {
                layoutUsername.isErrorEnabled = false
            }
        }
        rootView.txtPassword.doOnTextChanged { text, start, before, count ->
            if(!txtPassword.text.toString().isEmpty()) {
                layoutPassword.isErrorEnabled = false
            }
        }
        rootView.btnSignIn.setOnClickListener{
            if(validate()) {
                login()
            }
        }
        return rootView
    }
    private fun validate() : Boolean {
        if(rootView.txtUsername.text.toString().isEmpty()) {
            rootView.layoutUsername.isErrorEnabled = true
            rootView.layoutUsername.error = "Bạn cần nhập tên đăng nhập"
            return false
        }
        if(rootView.txtPassword.text.toString().isEmpty()) {
            rootView.layoutPassword.isErrorEnabled = true
            rootView.layoutPassword.error = "Bạn cần nhập mật khẩu"
            return false
        }
        return true
    }
    private fun login() {
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()

        val username = rootView.txtUsername.text.toString()
        val password = rootView.txtPassword.text.toString()
        val authClient: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
        val loginService: Call<JsonObject> = authClient.login(username, password)
        loginService.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.dismiss()
                pDialog = SweetAlertDialog(this@SignInFragment.context, SweetAlertDialog.ERROR_TYPE)
                pDialog.setTitleText("Lỗi...")
                    .setContentText(t.localizedMessage)
                    .show();
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                pDialog.dismiss()

                val lObject = response.body()!!
                if(response.isSuccessful&&lObject.get("success").asBoolean) {
                    val token = lObject.get("token").asString
                    val user = Gson().fromJson(lObject.get("user").asJsonObject, User::class.java)

                    val editor = MainApplication.userSharedPreferences().edit()
                    editor.putString("token", token)
                    editor.putString("username", user.username)
                    editor.putString("email", user.email)
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    this@SignInFragment.startActivity(Intent(this@SignInFragment.context, MainActivity::class.java))
                    this@SignInFragment.activity?.finish()
                } else {
                    pDialog = SweetAlertDialog(this@SignInFragment.context, SweetAlertDialog.ERROR_TYPE)
                    pDialog.setTitleText("Lỗi...")
                        .setContentText(lObject.get("message").asString)
                        .show();
                }
            }

        })
    }
}