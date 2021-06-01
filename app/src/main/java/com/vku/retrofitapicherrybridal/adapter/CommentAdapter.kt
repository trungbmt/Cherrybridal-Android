package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.model.PostComment
import kotlinx.android.synthetic.main.row_comment.view.*

class CommentAdapter(var comments : ArrayList<PostComment>, var context : Context) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar = itemView.imgAvatar
        var tvUsername = itemView.tvUsername
        var tvBody = itemView.body
        var btnLike = itemView.btnLike
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.row_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var comment = comments.get(position)
        holder.tvUsername.text = comment.user.username
        holder.tvBody.text = comment.content
        if(!comment.user.provider.isBlank()) {
            Log.d("USERIMG", "${comment.user.avatar}")
            Glide.with(context)
                .load(comment.user.avatar)
                .into(holder.imgAvatar)
        } else {
            Glide.with(context)
                    .load(AppConfig.IMAGE_URL+comment.user.avatar)
                    .into(holder.imgAvatar)
        }
    }
}