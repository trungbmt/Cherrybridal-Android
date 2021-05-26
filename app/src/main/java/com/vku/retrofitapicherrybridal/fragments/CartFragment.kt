package com.vku.retrofitapicherrybridal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.activities.DashboardActivity
import com.vku.retrofitapicherrybridal.activities.OrderActivity
import com.vku.retrofitapicherrybridal.adapter.CartAdapter
import com.vku.retrofitapicherrybridal.model.Cart
import com.vku.retrofitapicherrybridal.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.cart_list.view.*

class CartFragment : Fragment() {
    lateinit var rootView : View
    lateinit var cartViewModel: CartViewModel
    var carts = ArrayList<Cart>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.cart_list, container, false)
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.getCarts()
        cartViewModel.mCartsLiveData.observe(viewLifecycleOwner, Observer {
            var cartAdapater = CartAdapter(it, this.requireContext(), cartViewModel)
            carts = it
            rootView.cart_list_rv.adapter = cartAdapater
        })
        cartViewModel.mCostLiveData.observe(viewLifecycleOwner, Observer {
            rootView.total_cart_amount.text = Tools.format_currency(it)
        })

        rootView.cart_list_rv.layoutManager = LinearLayoutManager(this.context)

        rootView.btn_order.setOnClickListener {
            var jsonCarts = Gson().toJson(carts)
            var intent = Intent(context, OrderActivity::class.java)
            intent.putExtra("carts", jsonCarts)
            startActivityForResult(intent, 0)
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