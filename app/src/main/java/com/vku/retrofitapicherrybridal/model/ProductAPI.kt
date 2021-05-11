package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class ProductAPI() {
    var current_page : Int = 0

    @SerializedName("data")
    var products : List<Product> = ArrayList<Product>()

    var first_page_url : String = ""
    var from : Int = 0
    var last_page : Int = 0
    var last_page_url : String = ""
    var next_page_url : String = ""
    var path : String = ""
    var per_page : String = ""
    var prev_page_url : String = ""
    var to : Int = 0
    var total : Int = 0
}