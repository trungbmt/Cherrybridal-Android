package com.vku.retrofitapicherrybridal.model

import android.R.attr.path
import com.google.gson.annotations.SerializedName
import com.vku.retrofitapicherrybridal.AppConfig
import java.net.URLConnection


class Post() {
    var description = ""
    @SerializedName("media")
    var url = ""
    var poster : User = User()

    override fun toString(): String {
        return "Post(description='$description', url='$url', poster=${poster.username})"
    }
    fun getMediaUrl() : String {
        return AppConfig.IMAGE_URL+ url
    }
    fun isImageFile() : Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(url)
        return mimeType != null && mimeType.startsWith("image")
    }


}