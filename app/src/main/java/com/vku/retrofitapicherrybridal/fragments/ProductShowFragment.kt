package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.CategoryAdapter
import com.vku.retrofitapicherrybridal.viewmodel.ProductViewModel
import com.vku.retrofitapicherrybridal.viewmodel.ShopViewModel
import kotlinx.android.synthetic.main.fragment_shop.view.*
import kotlinx.android.synthetic.main.product_show.*

class ProductShowFragment () : Fragment() {
    lateinit var rootView : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.product_list, container, false)
        getProduct()
        return rootView
    }
    fun getProduct() {
        val bundle = this.arguments
        if (bundle != null) {
            val id = bundle.getInt("product_id", 1)
            val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
            productViewModel.getProduct(id)
            productViewModel.getProductLiveData().observe(this, Observer {
                tvName.text = it.name
            })
        }
    }
}