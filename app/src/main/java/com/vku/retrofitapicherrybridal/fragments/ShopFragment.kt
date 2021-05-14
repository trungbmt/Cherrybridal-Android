package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.CategoryAdapter
import com.vku.retrofitapicherrybridal.viewmodel.ShopViewModel
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_shop.view.*


class ShopFragment : Fragment() {
    var scrollPositionX : Int = 0
    var scrollPositionY : Int = 0
    private lateinit var rootView : View
    private lateinit var shopViewModel : ShopViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        shopViewModel.getCategoriesLiveData().observe(this, Observer {
            var categoryAdapter = CategoryAdapter(it, this.context!!)
            rootView.rv_category.adapter = categoryAdapter
        })
        rootView = inflater.inflate(R.layout.fragment_shop, container, false)
        rootView.rv_category.setHasFixedSize(true)
        rootView.rv_category.layoutManager = GridLayoutManager(this.context, 2)
        return rootView
    }
    override fun onPause() {
        super.onPause()
        // Save ListView state @ onPause
        scrollPositionX = rootView.scrollX
        scrollPositionY = rootView.scrollY
        Log.d("TAG", "saving fragment state = $scrollPositionX / $scrollPositionY")
    }

    override fun onResume() {
        super.onResume()
        rootViewScroll.smoothScrollTo(scrollPositionX,scrollPositionY)
        Log.d("TAG", "paste fragment state = $scrollPositionX / $scrollPositionY")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}