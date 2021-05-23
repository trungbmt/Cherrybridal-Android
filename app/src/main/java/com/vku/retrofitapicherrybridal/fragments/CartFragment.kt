package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vku.retrofitapicherrybridal.R

class CartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.cart_row, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CartFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}