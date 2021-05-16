package com.vku.retrofitapicherrybridal.model

import com.google.gson.annotations.SerializedName

class Post() {
    var description = ""
    @SerializedName("media")
    var url = ""
    var poster : User = User()

    override fun toString(): String {
        return "Post(description='$description', url='$url', poster=${poster.username})"
    }


}