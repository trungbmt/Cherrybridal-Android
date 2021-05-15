package com.vku.retrofitapicherrybridal.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.client.CategoryClient
import com.vku.retrofitapicherrybridal.fragments.BlogFragment
import com.vku.retrofitapicherrybridal.fragments.CartFragment
import com.vku.retrofitapicherrybridal.fragments.MenuFragment
import com.vku.retrofitapicherrybridal.fragments.ShopFragment
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call

class DashboardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dashboard)

        addFragment(BlogFragment.newInstance())
        bottomNavigation.show(0, false)
        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.ic_android_black_24dp))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.rings))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.cart))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.menu))
        var blogFragment : BlogFragment? = null
        var shopFragment : ShopFragment? = null
        bottomNavigation.setOnClickMenuListener{
            when (it.id){
                0 -> {
                    if(blogFragment==null) {
                        blogFragment = BlogFragment()
                        replaceFragment(blogFragment!!)
                    } else replaceFragment(blogFragment!!)
                }
                1 -> {
                    if(shopFragment==null) {
                        shopFragment = ShopFragment()
                        replaceFragment(shopFragment!!)
                    } else replaceFragment(shopFragment!!)
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
