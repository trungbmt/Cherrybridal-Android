package com.vku.retrofitapicherrybridal.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.activities.AuthActivity
import com.vku.retrofitapicherrybridal.activities.MainActivity
import com.vku.retrofitapicherrybridal.client.AuthClient
import com.vku.retrofitapicherrybridal.model.User
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInFragment : Fragment() {
    lateinit var rootView : View
    val RC_SIGN_IN : Int = 0
    val authClient: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
    var callbackManager = CallbackManager.Factory.create()
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(MainApplication.applicationContext().getString(R.string.server_client_id))
        .requestEmail()
        .build()

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
        rootView.fb_loginBtn.setReadPermissions("email");
        rootView.fb_loginBtn.setFragment(this);
        rootView.fb_loginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                if (loginResult != null) {
                    Log.d("LoginAPI", "Facebook login token: "+loginResult.accessToken.token)
                    socialite_login("facebook", loginResult.accessToken.token)
                }
            }
            override fun onCancel() {}
            override fun onError(exception: FacebookException) {Log.d("LoginAPI", "Facebook login error: ${exception.message}")}
        })

        rootView.gg_loginBtn.setSize(SignInButton.SIZE_STANDARD);
        rootView.gg_loginBtn.setOnClickListener {
            gg_login();
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
    private fun gg_login() {
        val mGoogleSignInClient = GoogleSignIn.getClient(this@SignInFragment.context as Activity, gso);
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private fun socialite_login(provider: String, access_token: String) {
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()
        val loginService: Call<JsonObject> = authClient.socialiteLogin(provider, access_token)
        loginService.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.dismiss()
                pDialog = SweetAlertDialog(this@SignInFragment.context, SweetAlertDialog.ERROR_TYPE)
                pDialog.setTitleText("Lỗi...")
                    .setContentText(t.localizedMessage)
                    .show();
                Log.d("messageback", t.message.toString())
                t.stackTrace.forEach { e ->
                    Log.d("messageback", e.toString())
                }
                AuthActivity.logout_gg(this@SignInFragment.context!!)
                AuthActivity.logout_fb()

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
                    Log.d("messageback", response.body().toString())
                    AuthActivity.logout_gg(this@SignInFragment.context!!)
                    AuthActivity.logout_fb()
                }
            }

        })
    }
    private fun login() {
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()

        val username = rootView.txtUsername.text.toString()
        val password = rootView.txtPassword.text.toString()
        val loginService: Call<JsonObject> = authClient.login(username, password)
        loginService.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.dismiss()
                pDialog = SweetAlertDialog(this@SignInFragment.context, SweetAlertDialog.ERROR_TYPE)
                pDialog.setTitleText("Lỗi...")
                    .setContentText(t.localizedMessage)
                    .show();
                Log.d("messageback", t.message.toString())
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
                    Log.d("messageback", response.body().toString())
                }
            }

        })
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        //facebook callback
        callbackManager.onActivityResult(requestCode, resultCode, data)

        //gooogle callback
        if (requestCode === RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val token = account?.idToken
            if(token!=null) {
                Log.d("LoginAPI", "Token google :" + token)
                socialite_login("google", token)
            }
        } catch (e: ApiException) {
            Log.d("LoginAPI", "Login google failed code =" + e.getStatusCode())
        }
    }
}