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
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.layoutPassword
import kotlinx.android.synthetic.main.fragment_sign_up.view.layoutUsername
import kotlinx.android.synthetic.main.fragment_sign_up.view.txtPassword
import kotlinx.android.synthetic.main.fragment_sign_up.view.txtUsername
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {
    lateinit var rootView : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_sign_up, container,false)
        rootView.txtSignIn.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.frameAuthContainer, SignInFragment()).commit()
        }

        rootView.txtUsername.doOnTextChanged { text, start, before, count ->
            if(!rootView.txtUsername.text.toString().isEmpty()) {
                layoutUsername.isErrorEnabled = false
            }
        }
        rootView.txtEmail.doOnTextChanged { text, start, before, count ->
            if(isValidEmail(rootView.txtEmail.text.toString())) {
                layoutEmail.isErrorEnabled = false
            }
        }
        rootView.txtPassword.doOnTextChanged { text, start, before, count ->
            if(rootView.txtPassword.text.toString().length>=6) {
                layoutPassword.isErrorEnabled = false
            }
        }
        rootView.txtConfirm.doOnTextChanged { text, start, before, count ->
            if(!rootView.txtConfirm.text.toString().isEmpty()||rootView.txtConfirm.text.toString().equals(rootView.txtPassword.text.toString())) {
                layoutConfirm.isErrorEnabled = false
            }
        }
        rootView.btnSignUp.setOnClickListener{
            if(validate()) {
                register()
            }
        }
        return rootView
    }

    private fun validate() : Boolean {
        if(rootView.txtUsername.text.toString().isEmpty()) {
            rootView.layoutUsername.isErrorEnabled = true
            rootView.layoutUsername.error = "Bạn cần nhập tên đăng nhập!"
            return false
        }
        if(!isValidEmail(rootView.txtEmail.text.toString())) {
            rootView.layoutEmail.isErrorEnabled = true
            rootView.layoutEmail.error = "Địa chỉ email không hợp lệ!"
            return false
        }
        if(rootView.txtPassword.text.toString().length<6) {
            rootView.layoutPassword.isErrorEnabled = true
            rootView.layoutPassword.error = "Mật khẩu phải có ít nhất 6 kí tự!"
            return false
        }
        if(rootView.txtConfirm.text.toString().isEmpty()||!rootView.txtConfirm.text.toString().equals(rootView.txtPassword.text.toString())) {
            rootView.layoutConfirm.isErrorEnabled = true
            rootView.layoutConfirm.error = "Nhập lại mật khẩu không khớp!"
            return false
        }
        return true
    }
    private fun isValidEmail(target : String) : Boolean {
        if(target.isEmpty()) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private fun register() {
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()

        val username = rootView.txtUsername.text.toString()
        val email = rootView.txtEmail.text.toString()
        val password = rootView.txtPassword.text.toString()
        val password_confirmation = rootView.txtConfirm.text.toString()
        val authClient: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
        val registerService: Call<JsonObject> = authClient.register(username, email, password, password_confirmation)
        registerService.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.hide()
                pDialog = SweetAlertDialog(this@SignUpFragment.context, SweetAlertDialog.ERROR_TYPE)
                pDialog.setTitleText("Lỗi....")
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
                    pDialog = SweetAlertDialog(this@SignUpFragment.context, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog.setTitleText("Thành công!")
                        .setContentText("Tài khoản của bạn đã được khởi tạo")
                        .show();
                    pDialog.setConfirmClickListener {
                        this@SignUpFragment.startActivity(Intent(this@SignUpFragment.context, MainActivity::class.java))
                        this@SignUpFragment.activity?.finish()
                    }

                } else {
                    pDialog = SweetAlertDialog(this@SignUpFragment.context, SweetAlertDialog.ERROR_TYPE)
                    pDialog.setTitleText("Lỗi...")
                        .setContentText(lObject.get("message").asString)
                        .show();
                }
            }

        })
    }
}