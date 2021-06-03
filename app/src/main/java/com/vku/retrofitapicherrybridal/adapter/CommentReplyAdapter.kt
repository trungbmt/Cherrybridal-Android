package com.vku.retrofitapicherrybridal.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.client.CommentClient
import com.vku.retrofitapicherrybridal.model.PostComment
import com.vku.retrofitapicherrybridal.model.PostCommentAPI
import kotlinx.android.synthetic.main.row_comment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentReplyAdapter(var comments : ArrayList<PostComment>, var context : Context) : RecyclerView.Adapter<CommentReplyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar = itemView.imgAvatar
        var tvUsername = itemView.tvUsername
        var tvBody = itemView.body
        var tvLikeNum = itemView.tvLikeCount
        var btnLike = itemView.btnLike

        init {
            btnLike.setOnClickListener {
                likeComment(absoluteAdapterPosition, tvLikeNum, btnLike)
            }
        }
    }
    fun likeComment(position: Int, tvLikeNum : TextView, btnLike : ImageView) {
        var comment = comments.get(position)
        if(comment.liked) {
            comment.liked = false
            btnLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike))
        } else {
            comment.liked = true
            btnLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like))
        }

        var headers = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        if(token!=null) {
            headers.put("Authorization", "Bearer " + token)
        }
        val commentService = AppConfig.retrofit.create(CommentClient::class.java).likeComment(headers, comment.id)
        commentService.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("LIKECMT", "Failed ${t.message}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("LIKECMT", "Response ${response.body()}")
                tvLikeNum.text = response.body()?.get("likesNum")?.asInt.toString()
            }

        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.row_comment_reply, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var comment = comments.get(position)
        holder.tvUsername.text = comment.user.username
        holder.tvBody.text = comment.content
        holder.tvLikeNum.text = comment.likesNum.toString()
        if(!comment.user.provider.isBlank()) {
            Glide.with(context)
                    .load(comment.user.avatar)
                    .into(holder.imgAvatar)
        } else {
            Glide.with(context)
                    .load(AppConfig.IMAGE_URL+comment.user.avatar)
                    .into(holder.imgAvatar)
        }
        if(comment.liked) {
            holder.btnLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_like))
        } else {
            holder.btnLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_dislike))
        }
    }
}