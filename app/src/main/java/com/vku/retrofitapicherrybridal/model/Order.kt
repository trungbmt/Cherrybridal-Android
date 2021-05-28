package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

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
        if(status==0) return "Đang xử lí"
        if(status==-1) return "Đã huỷ đơn"
        return "Đã giao hàng"
    }
}