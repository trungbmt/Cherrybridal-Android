package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.activities.DashboardActivity
import com.vku.retrofitapicherrybridal.adapter.CartAdapter
import com.vku.retrofitapicherrybridal.model.Cart
import com.vku.retrofitapicherrybridal.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.cart_list.view.*

class CartFragment : Fragment() {
    lateinit var rootView : View
    lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.cart_list, container, false)
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.getCarts()
        cartViewModel.mCartsLiveData.observe(this, Observer {
            var cartAdapater = CartAdapter(it, this.context!!, cartViewModel)
            rootView.cart_list_rv.adapter = cartAdapater
        })
        cartViewModel.mCostLiveData.observe(this, Observer {
            rootView.total_cart_amount.text = Tools.format_currency(it)
        })

        rootView.cart_list_rv.layoutManager = LinearLayoutManager(this.context)

        rootView.btn_order.setOnClickListener {
//            (this.context as DashboardActivity).replaceOtherFragment()
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CartFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}