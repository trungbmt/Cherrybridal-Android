package com.vku.retrofitapicherrybridal.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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


class CommentAdapter(var comments : ArrayList<PostComment>, var context : Context) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgAvatar = itemView.imgAvatar
        var tvUsername = itemView.tvUsername
        var tvBody = itemView.body
        var tvLikeNum = itemView.tvLikeCount
        var btnLike = itemView.btnLike
        var tvReplies =itemView.tvReplies
        var rvReply = itemView.rvReply

        init {
            btnLike.setOnClickListener {
                likeComment(absoluteAdapterPosition, tvLikeNum, btnLike)
            }
            itemView.setOnClickListener {
                replyComment(absoluteAdapterPosition)
            }
            tvReplies.setOnClickListener {
                showReply(absoluteAdapterPosition, rvReply, tvReplies)
            }
        }
    }
    fun showReply(position: Int, rvReply: RecyclerView, tvReplies : TextView) {
        val comment = comments.get(position)

        var headers = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        if(token!=null) {
            headers.put("Authorization", "Bearer " + token)
            val commentService = AppConfig.retrofit.create(CommentClient::class.java).getReplies(headers, comment.id)
            commentService.enqueue(object : Callback<PostCommentAPI>{
                override fun onFailure(call: Call<PostCommentAPI>, t: Throwable) {
                    Log.d("REPLIES", "Fail ${t.message}")
                }

                override fun onResponse(call: Call<PostCommentAPI>, response: Response<PostCommentAPI>) {
                    Log.d("REPLIES", "Response ${response.body()}")
                    var replies = response.body()?.comments
                    if(replies!=null && replies.size>0) {
                        tvReplies.visibility = View.GONE
                        rvReply.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        rvReply.adapter = CommentReplyAdapter(replies, context)
                        rvReply.visibility = View.VISIBLE
                    }
                }

            })
        }
    }
    fun replyComment(position: Int) {
        val comment = comments.get(position)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Reply ${comment.user.username}")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Send") { dialog, which ->
            Log.d("REPLYCMT", "message ${input.text.toString()}")
            if(!input.text.toString().isBlank()) {
                var headers = HashMap<String, String>()
                var token = MainApplication.userSharedPreferences().getString("token", null)
                if(token!=null) {
                    headers.put("Authorization", "Bearer " + token)
                    var options = HashMap<String, String>()
                    options.put("post_id", "${comment.post_id}")
                    options.put(("content"), input.text.toString())
                    val commentService = AppConfig.retrofit.create(CommentClient::class.java).replyComment(headers, comment.id, options)
                    commentService.enqueue(object : Callback<PostComment>{
                        override fun onFailure(call: Call<PostComment>, t: Throwable) {
                            Log.d("REPLYCMT", "Fail ${t.message}")
                        }

                        override fun onResponse(call: Call<PostComment>, response: Response<PostComment>) {
                            Log.d("REPLYCMT", "Response ${response.body()}")
                            val replyComment = response.body()
                            if(comment!=null) {
                                comment.repliesNum++
                                notifyItemChanged(position)
                            }
                        }

                    })
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
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
        if(comment.repliesNum>0) {
            holder.tvReplies.text = "View replies (${comment.repliesNum})"
            holder.tvReplies.visibility = View.VISIBLE
        } else {
            holder.tvReplies.visibility = View.GONE
        }
    }
}