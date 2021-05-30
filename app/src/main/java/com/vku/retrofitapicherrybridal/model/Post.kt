package com.vku.retrofitapicherrybridal.model

import android.R.attr.path
import com.google.gson.annotations.SerializedName
import com.vku.retrofitapicherrybridal.AppConfig
import java.net.URLConnection


class Post() {
    var id = 0
    var description = ""
    @SerializedName("media")
    var url = ""
    var poster : User = User()
    var self_like = 0
    var liked = false
    var commentCount = 0


    fun getMediaUrl() : String {
        return AppConfig.IMAGE_URL+ url
    }
    fun isImageFile() : Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(url)
        return mimeType != null && mimeType.startsWith("image")
    }

    override fun toString(): String {
        return "Post(id=$id, description='$description', url='$url', poster=$poster, self_like=$self_like, liked=$liked)"
    }


}