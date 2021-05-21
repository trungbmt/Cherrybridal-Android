package com.vku.retrofitapicherrybridal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.fragments.ProductShowFragment
import com.vku.retrofitapicherrybridal.fragments.ShopProductFragment
import kotlinx.android.synthetic.main.product_show.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
//        loginToken()
//        var bundle = Bundle()
//        bundle.putInt("product_id", 1)
//
//        var productShowFragment = ProductShowFragment()
//        productShowFragment.arguments = bundle
        var shopProductFragment = ShopProductFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, shopProductFragment).commit()
    }



}