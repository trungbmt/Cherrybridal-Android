package com.vku.retrofitapicherrybridal.adapter

import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.danikula.videocache.HttpProxyCacheServer
import com.facebook.share.model.ShareContent
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.ShareMediaContent
import com.facebook.share.model.ShareVideo
import com.facebook.share.widget.ShareDialog
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.model.Post
import kotlinx.android.synthetic.main.single_post_row.view.*


class PostAdapter(var posts : ArrayList<Post>, var context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val proxy : HttpProxyCacheServer = HttpProxyCacheServer(this.context)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var background = itemView.textureView
        var imgview = itemView.imageView
        var btnPlay = itemView.btnPlay
        var url : String? = null
        var btnShare = itemView.btnShare
        var mediaPlayer : MediaPlayer = MediaPlayer()
        init {

            mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                    Log.d("ERRORPOST", "postionz $absoluteAdapterPosition what $what")
                    return true
                }
            })
            background.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                }
                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                }
                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    return true
                }
                override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                    Log.d("onSurface","created $absoluteAdapterPosition")
                    mediaPlayer.setSurface(Surface(surface))
                    try {
                        mediaPlayer.seekTo(0)
                    } catch (e: Exception) {}
                }
            }
            background.setOnClickListener {
                if(mediaPlayer.isPlaying) {

                    mediaPlayer.pause()
                    btnPlay.visibility = View.VISIBLE
                } else {
                    mediaPlayer.start()
                    btnPlay.visibility = View.GONE
                }
            }
        }
        fun selectedView(position: Int) {
            if(position==0) {
                Handler(Looper.getMainLooper()).postDelayed({
                    mediaPlayer.start()
                }, 100)
            }
            else mediaPlayer.start()
        }
        fun unSelected() {
            mediaPlayer.stop()
            mediaPlayer.prepareAsync()
        }
        fun pauseVideo() {
            try {
                mediaPlayer.stop()
            } catch (e: Exception) {}
        }
        var poster = itemView.tvPoster
        var title = itemView.tvTitle
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.single_post_row, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.poster.text = post.poster.username
        holder.title.text = post.description

        holder.btnShare.setOnClickListener {
            val shareContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(post.getMediaUrl()))
                .setQuote(post.poster.username+": "+ post.description)
                .build();
            ShareDialog.show(context as Activity, shareContent);
        }
        val proxyUrl = proxy.getProxyUrl(post.getMediaUrl())
        if(!post.isImageFile()) {
            holder.background.visibility = View.VISIBLE
            holder.imgview.visibility = View.GONE
            try {
                holder.mediaPlayer = MediaPlayer()
                holder.mediaPlayer.setDataSource(proxyUrl)
                holder.mediaPlayer.prepareAsync()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("PostInfo", "setDataSource ERROR proxy $proxyUrl, post URL $post.getMediaUrl()")
            }
            holder.mediaPlayer.setOnCompletionListener {
                it.start()
            }
            holder.mediaPlayer.setOnPreparedListener {
                it.seekTo(0)
            }

            holder.mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
                override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                    Log.d("ERRORPOST", "postion $position what $what, background ${holder.background.isAvailable}")
                    if(what==1) {
                        holder.mediaPlayer.release()
                    }
                    return true
                }
            })
        } else {
            Glide.with(context)
                    .asDrawable()
                    .load(proxyUrl)
                    .into(object : CustomTarget<Drawable>(){
                        override fun onLoadCleared(placeholder: Drawable?) {}
                        override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                        ) {
                            holder.mediaPlayer.stop()
                            holder.background.visibility = View.GONE
                            holder.imgview.setImageDrawable(resource)
                            holder.imgview.visibility = View.VISIBLE
                        }
                    })
        }
        Log.d("PostInfo", "Post: $position proxy $proxyUrl, post URL ${post.getMediaUrl()}")
    }

}