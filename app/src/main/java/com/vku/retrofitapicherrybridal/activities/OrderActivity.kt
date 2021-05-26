package com.vku.retrofitapicherrybridal.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.OrderItemAdapater
import com.vku.retrofitapicherrybridal.model.Cart
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_order.abort_btn


class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        var jsonCarts = intent.getStringExtra("carts")
        val myType = object : TypeToken<ArrayList<Cart>>() {}.type
        var carts = Gson().fromJson<ArrayList<Cart>>(jsonCarts, myType)
        Log.d("CHECK1", "${carts.size}")
        var orderItemAdapater = OrderItemAdapater(carts)
        order_item_rv.layoutManager = LinearLayoutManager(this)
        order_item_rv.adapter = orderItemAdapater
        Log.d("CHECK2", ""+orderItemAdapater.items.toString())

        abort_btn.setOnClickListener {
            finish()
        }
    }
}