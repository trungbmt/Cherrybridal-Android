package com.example.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

class Category() {

    @SerializedName("category_name")
    var name : String = ""

    @SerializedName("category_desc")
    var desc : String = ""

    @SerializedName("category_img")
    var img_link : String = ""

    override fun toString(): String {
        return "name: "+name+" | desc: "+desc+" | img_link: "+img_link
    }
}