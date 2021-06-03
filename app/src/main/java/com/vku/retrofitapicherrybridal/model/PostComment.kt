package com.vku.retrofitapicherrybridal.model

class PostComment() {
    var id = 0
    var content = ""
    var post_id = 0
    var user = User()
    var repliesNum = 0
    var likesNum = 0
    var reply_id : Int? = null

    var liked : Boolean = false
    override fun toString(): String {
        return "PostComment(id=$id, content='$content', repliesNum=$repliesNum, likesNum=$likesNum, liked=$liked)"
    }


}