package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class Product() {
    @SerializedName("product_id")
    var id : Int = 0

    @SerializedName("product_category")
    var category_id : Int = 0

    @SerializedName("product_name")
    var name : String = ""

    @SerializedName("product_desc")
    var desc : String = ""

    @SerializedName("product_tag")
    var tag : Int = 0

    @SerializedName("product_img")
    var img : String = ""

    @SerializedName("product_details")
    var productDetails = ArrayList<ProductDetail>()
}