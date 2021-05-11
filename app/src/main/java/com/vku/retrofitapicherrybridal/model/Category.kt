package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class Category() {
    @SerializedName("category_id")
    var id : Int = 0

    @SerializedName("category_name")
    var name : String = ""

    @SerializedName("category_desc")
    var desc : String = ""

    @SerializedName("category_img")
    var img : String = ""

    override fun toString(): String {
        return "name: "+name+" | desc: "+desc+" | img_link: "+img
    }
}