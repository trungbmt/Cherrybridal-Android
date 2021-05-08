package com.example.retrofitapicherrybridal.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofitapicherrybridal.R


class SplashActivity : AppCompatActivity() {

    //Var
    val SPLASH_SCREEN = 3000
    var topAnim: Animation? = null
    var bottomAnim: Animation? = null
    var image: ImageView? = null
    var logo: TextView? = null
    var slogan: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        //Animate
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        //Hooks
        image = findViewById(R.id.imageView)
        logo = findViewById(R.id.textView)
        slogan = findViewById(R.id.textView2)
        image?.animation = topAnim
        logo?.animation = bottomAnim
        slogan?.animation = bottomAnim
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, AuthActivity::class.java)
            val pairs0 = Pair<View, String>(image, "logo_image")
            val pairs1 = Pair<View, String>(logo, "logo_text")
            val pairs: Array<Pair<View, String>> = arrayOf(pairs0, pairs1)
            val options = ActivityOptions.makeSceneTransitionAnimation(this@SplashActivity, *pairs)
            startActivity(intent, options.toBundle())
            finish()
        }, SPLASH_SCREEN.toLong())
    }

}

