package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vku.retrofitapicherrybridal.R

class OrderFragment : Fragment () {
    lateinit var rootView : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.order_list, container, false)


        return rootView
    }
}