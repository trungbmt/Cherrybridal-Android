package com.vku.retrofitapicherrybridal.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.client.AccountClient
import com.vku.retrofitapicherrybridal.client.CategoryClient
import com.vku.retrofitapicherrybridal.fragments.*
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.header_menu.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var mDrawerLayout : DrawerLayout
    var blogFragment : BlogFragment? = null
    var shopFragment : ShopFragment? = null
    var currentFragment : Fragment? = null
    var previousId = 0
    var backNav = true
    val IMAGE_PICKER_CODE = 0

    fun loadUserData() {
        val sPref = MainApplication.userSharedPreferences()
        val uAvatar = sPref.getString("avatar", null)
        if(uAvatar!=null) {
            var provider = sPref.getString("provider", null)
            if(provider!=null && !provider.isBlank()) {
                Glide.with(this)
                        .load(uAvatar)
                        .into(nav_menu.getHeaderView(0).imgAvatar)
                nav_menu.getHeaderView(0).layoutChangeAvt.visibility = View.GONE
            } else {
                Glide.with(this)
                        .load(AppConfig.IMAGE_URL+uAvatar)
                        .into(nav_menu.getHeaderView(0).imgAvatar)
            }
        }
        val uName = sPref.getString("username", null)
        if(uName!=null) {
            nav_menu.getHeaderView(0).tvUsername.text = uName
        }
    }
    fun imagePick() {

        val pickIntent = Intent(
                Intent.ACTION_PICK
        )
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICKER_CODE) {
            val imageUri = data?.data
            if(imageUri!=null) {
                val realPath = Tools.getRealPathFromURI(imageUri, this)
                val file = File(realPath)
                Log.d("AVTUPLOAD", "File: ${file.absolutePath}")
                var requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                var body: MultipartBody.Part =
                        MultipartBody.Part.createFormData("avatar", file.name, requestFile)

                var headers = HashMap<String, String>()
                var token = MainApplication.userSharedPreferences().getString("token", null)
                headers.put("Authorization", "Bearer " + token)
                val accountService = AppConfig.retrofit.create(AccountClient::class.java).changeAvatar(headers, body)

                var pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                pDialog.setCancelable(false)
                pDialog.show()

                accountService.enqueue(object : Callback<JsonObject>{
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        pDialog.dismiss()
                        pDialog = SweetAlertDialog(this@DashboardActivity, SweetAlertDialog.ERROR_TYPE)
                        pDialog.titleText = "Thay ảnh đại diện thất bại!"
                        pDialog.show()
                        Log.d("AVTUPLOAD", "Failed ${t.message}")
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        pDialog.dismiss()
                        pDialog = SweetAlertDialog(this@DashboardActivity, SweetAlertDialog.SUCCESS_TYPE)
                        pDialog.titleText = response.body()?.get("message")?.asString
                        pDialog.show()

                        var editor = MainApplication.userSharedPreferences().edit()
                        val avatar = response.body()?.get("avatar")?.asString
                        Glide.with(this@DashboardActivity)
                                .load(AppConfig.IMAGE_URL+avatar)
                                .into(nav_menu.getHeaderView(0).imgAvatar)
                        editor.putString("avatar", avatar)
                        editor.apply()
                        Log.d("AVTUPLOAD", "Response ${response.body().toString()}")
                    }

                })

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dashboard)

        mDrawerLayout = dashboard_drawer
        nav_menu.setNavigationItemSelectedListener(this)
        nav_menu.itemIconTintList = null
        nav_menu.getHeaderView(0).btnAvtChange.setOnClickListener {
            imagePick()
        }


        loadUserData()

        blogFragment = BlogFragment.newInstance()
        addFragment(blogFragment!!)
        currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        bottomNavigation.show(0, false)
        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.ic_blog))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.rings))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.cart))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.menu))
        bottomNavigation.setOnClickMenuListener{
            when (it.id){
                0 -> {
                    previousId = 0
                    if(blogFragment==null) {
                        blogFragment = BlogFragment()
                        replaceFragment(blogFragment!!)
                    } else replaceFragment(blogFragment!!)
                }
                1 -> {
                    previousId = 1
                    if(shopFragment==null) {
                        shopFragment = ShopFragment()
                        replaceFragment(shopFragment!!)
                    } else replaceFragment(shopFragment!!)
                }
                2 -> {
                    previousId = 2
                    replaceFragment(CartFragment.newInstance())
                }
                3 -> {
                    if(mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                        mDrawerLayout.closeDrawer(GravityCompat.END)
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.END)
                    }
                }
                else -> {
                    replaceFragment(BlogFragment.newInstance())
                }
            }
            currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        }
        mDrawerLayout.addDrawerListener(object : ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.chery_bridal, R.drawable.chery_bridal) {
            override fun onDrawerClosed(drawerView: View) {
                if(backNav) {
                    bottomNavigation.show(previousId)

                } else {
                    backNav = true
                }
                super.onDrawerClosed(drawerView)
            }
        })

    }


    public fun backToBlogFragment() {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,blogFragment as Fragment).commit()
        bottomNavigation.show(0, true)
    }
    fun replaceOtherFragment(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).commit()
        fragmentTransition.addToBackStack(null)
    }
    private fun replaceFragment(fragment:Fragment){

        blogFragment?.pauseMusic()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).commit()
    }

    private fun addFragment(fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,fragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.create_post -> {
                replaceFragment(NewPostFragment())
                bottomNavigation.show(-1, true)
                backNav = false
            }
            R.id.order_list -> {
                replaceFragment(OrderFragment())
                bottomNavigation.show(-1, true)
                backNav = false
            }
            R.id.logout -> {
                blogFragment?.pauseMusic()
                AuthActivity.logout()
                AuthActivity.logout_fb()
                AuthActivity.logout_gg(this)
            }
            else -> {
                backNav = true
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.END)
        return true
    }
}
