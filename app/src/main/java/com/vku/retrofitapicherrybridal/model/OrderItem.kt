package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class OrderItem {
    var id = 0

    @SerializedName("product_api")
    var product = Product()

    @SerializedName("detail_api")
    var detail = ProductDetail()

    var quantity = 0
}