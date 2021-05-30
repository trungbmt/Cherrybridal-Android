package com.vku.retrofitapicherrybridal.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.gson.annotations.SerializedName
import com.vku.retrofitapicherrybridal.R

class Order {
    @SerializedName("order_id")
    var id = 0
    @SerializedName("order_full_name")
    var name = ""

    @SerializedName("order_phone")
    var phone = ""

    @SerializedName("order_status")
    var status = 0

    @SerializedName("order_city")
    var city = ""

    @SerializedName("order_province")
    var province = ""

    @SerializedName("order_address")
    var address = ""

    @SerializedName("items_api")
    var items = ArrayList<OrderItem>()

    @SerializedName("created_at")
    var created_at = ""

    fun getPrice() : Int {
        var price = 0
        items.forEach {
            price+= it.detail.price*it.quantity
        }
        return price
    }
    fun getStatus() : String {
        if(status==0) return "Processing"
        if(status==-1) return "Canceled"
        return "Delivered"
    }
    fun getIconId() : Int {
        if(status==0) return R.drawable.ic_shiping
        if(status==-1) return R.drawable.ic_abort_order
        return R.drawable.ic_success
    }
}