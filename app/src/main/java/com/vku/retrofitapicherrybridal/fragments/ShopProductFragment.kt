package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.CategoryAdapter
import com.vku.retrofitapicherrybridal.adapter.ProductAdapater
import com.vku.retrofitapicherrybridal.viewmodel.ProductViewModel
import com.vku.retrofitapicherrybridal.viewmodel.ShopViewModel
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.android.synthetic.main.fragment_product_list.view.*
import kotlinx.android.synthetic.main.fragment_shop.view.*

class ShopProductFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var productViewModel : ProductViewModel
    private var category = -1
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_product_list, container, false)

        var options = HashMap<String, String>()
        val bundle = this.arguments
        if (bundle != null) {
            category = bundle.getInt("category_id")
            if(category!=-1) {
                options.put("category", category.toString())
            }
        }

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.getProducts(options)
        productViewModel.getProductsLiveData().observe(viewLifecycleOwner, Observer {
            var productAdapter = ProductAdapater(it, this.requireContext())
            rootView.rv_product.adapter = productAdapter
        })

        rootView.rv_product.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(this.context, 2)
        rootView.rv_product.layoutManager = gridLayoutManager
        rootView.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if(scrollY>0) {
                if(!productViewModel.isLoading) {
                    rootView.progress_bar.visibility = View.GONE
                    val currentItems = gridLayoutManager.childCount
                    val totalItems = gridLayoutManager.itemCount
                    val scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition()
                    if (currentItems + scrollOutItems == totalItems) {
                        productViewModel.loadMore(options)
                    }
                    Log.d("CHECKK", "Current $currentItems, Total $totalItems, scrollOut $scrollOutItems")
                } else {
                    rootView.progress_bar.visibility = View.VISIBLE

                }
            }
        }
        rootView.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    options.put("search", query)
                    productViewModel.getProducts(options)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(TextUtils.isEmpty(newText)) {
                    options.remove("search")
                    productViewModel.getProducts(options)
                }
                return true
            }

        })
        return rootView
    }
    fun loadMore() {

    }
}