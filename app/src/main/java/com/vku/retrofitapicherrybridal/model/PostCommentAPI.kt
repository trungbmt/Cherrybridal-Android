package com.vku.retrofitapicherrybridal.model

class PostCommentAPI {
    var success = false
    var comments = ArrayList<PostComment>()
    override fun toString(): String {
        return "PostCommentAPI(success=$success, comments=$comments)"
    }

}