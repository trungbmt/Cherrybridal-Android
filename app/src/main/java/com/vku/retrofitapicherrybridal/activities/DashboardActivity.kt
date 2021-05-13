package com.vku.retrofitapicherrybridal.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.fragments.BlogFragment
import com.vku.retrofitapicherrybridal.fragments.CartFragment
import com.vku.retrofitapicherrybridal.fragments.MenuFragment
import com.vku.retrofitapicherrybridal.fragments.ShopFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dashboard)

        addFragment(BlogFragment.newInstance())
        bottomNavigation.show(0, true)
        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.blog))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.rings))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.cart))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.menu))

        bottomNavigation.setOnClickMenuListener{
            when (it.id){
                0 -> {
                    replaceFragment(BlogFragment.newInstance())
                }
                1 -> {
                    replaceFragment(ShopFragment.newInstance())
                }
                2 -> {
                    replaceFragment(CartFragment.newInstance())
                }
                3 -> {
                    replaceFragment(MenuFragment.newInstance())
                }
                else -> {
                    replaceFragment(BlogFragment.newInstance())
                }
            }
        }
    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
        }

    private fun addFragment(fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }
    }
