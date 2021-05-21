package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.CategoryAdapter
import com.vku.retrofitapicherrybridal.adapter.ProductAdapater
import com.vku.retrofitapicherrybridal.viewmodel.ProductViewModel
import com.vku.retrofitapicherrybridal.viewmodel.ShopViewModel
import kotlinx.android.synthetic.main.fragment_product_list.view.*
import kotlinx.android.synthetic.main.fragment_shop.view.*

class ShopProductFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var productViewModel : ProductViewModel
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_product_list, container, false)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getProducts()
        productViewModel.getProductsLiveData().observe(this, Observer {
            var productAdapter = ProductAdapater(it, this.context!!)
            rootView.rv_product.adapter = productAdapter
        })
        rootView.rv_category.setHasFixedSize(true)
        rootView.rv_category.layoutManager = LinearLayoutManager(this.context)
        return rootView
    }
}