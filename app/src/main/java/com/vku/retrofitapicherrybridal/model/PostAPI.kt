package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class PostAPI() {
    var current_page : Int = 0

    @SerializedName("data")
    var posts : ArrayList<Post> = ArrayList<Post>()

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

    override fun toString(): String {
        return "PostAPI(current_page=$current_page, posts=$posts, first_page_url='$first_page_url', from=$from, last_page=$last_page, last_page_url='$last_page_url', next_page_url='$next_page_url', path='$path', per_page='$per_page', prev_page_url='$prev_page_url', to=$to, total=$total)"
    }

}