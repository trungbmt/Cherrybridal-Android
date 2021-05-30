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
import com.vku.retrofitapicherrybridal.adapter.OrderAdapter
import com.vku.retrofitapicherrybridal.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.cart_list.view.*
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderFragment : Fragment() {
    lateinit var rootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_order, container, false)
        rootView.rv.layoutManager = LinearLayoutManager(this.context)

        val orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        orderViewModel.getOrders()
        orderViewModel.mOrderLiveData.observe(viewLifecycleOwner, Observer {
            var orderAdapter = OrderAdapter(it, requireContext(), orderViewModel)
            rootView.rv.adapter = orderAdapter
        })

        return rootView
    }
}