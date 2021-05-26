package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class Cart(){
    @SerializedName("cart_id")
    var id = 0

    var product = Product()

    @SerializedName("product_detail")
    var detail = ProductDetail()

    var amount = 0
}