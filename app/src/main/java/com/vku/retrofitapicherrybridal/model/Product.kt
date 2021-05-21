package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName
import com.vku.retrofitapicherrybridal.AppConfig

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

    @SerializedName("lowest_price")
    var price : Int = 0

    @SerializedName("rating_value")
    var rating : Float = 0F

    @SerializedName("product_details")
    var productDetails = ArrayList<ProductDetail>()

    fun getTotalAmount() : Int {
        var total = 0
        productDetails.forEach {
            total+= it.amount
        }
        return total
    }
    fun getImageUrl() : String {
        return AppConfig.IMAGE_URL+ this.img
    }
}