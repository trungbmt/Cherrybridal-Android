package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.CategoryAdapter
import com.vku.retrofitapicherrybridal.client.CategoryClient
import com.vku.retrofitapicherrybridal.model.Category
import com.vku.retrofitapicherrybridal.model.CategoryAPI
import com.vku.retrofitapicherrybridal.viewmodel.ShopViewModel
import kotlinx.android.synthetic.main.fragment_shop.*
import kotlinx.android.synthetic.main.fragment_shop.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopFragment : Fragment() {

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

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}