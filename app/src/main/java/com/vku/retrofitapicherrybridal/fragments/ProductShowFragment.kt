package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vku.retrofitapicherrybridal.R

class ProductShowFragment () : Fragment() {
    lateinit var rootView : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.product_show, container, false)

        return rootView
    }
    fun getProduct() {
        val bundle = this.arguments
        if (bundle != null) {
            val id = bundle.getInt("product_id", 1)

        }
    }
}