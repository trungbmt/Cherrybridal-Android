package com.vku.retrofitapicherrybridal.activities

import android.os.Bundle
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
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.navigation.NavigationView
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.client.CategoryClient
import com.vku.retrofitapicherrybridal.fragments.*
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var mDrawerLayout : DrawerLayout
    var blogFragment : BlogFragment? = null
    var shopFragment : ShopFragment? = null
    var currentFragment : Fragment? = null
    var previousId = 0
    var backNav = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dashboard)
        mDrawerLayout = dashboard_drawer
        nav_menu.setNavigationItemSelectedListener(this)
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
