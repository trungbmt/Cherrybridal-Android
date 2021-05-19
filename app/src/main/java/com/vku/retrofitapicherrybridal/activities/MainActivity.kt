package com.vku.retrofitapicherrybridal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.vku.retrofitapicherrybridal.R
import kotlinx.android.synthetic.main.product_show.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.product_show)
        scrollView.isHorizontalScrollBarEnabled = false
//        loginToken()
    }



}