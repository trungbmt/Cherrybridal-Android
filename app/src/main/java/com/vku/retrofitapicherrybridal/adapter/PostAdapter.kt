package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.graphics.SurfaceTexture
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.danikula.videocache.HttpProxyCacheServer
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.model.Post
import kotlinx.android.synthetic.main.single_post_row.view.*
import java.io.File
import java.lang.Exception


class PostAdapter(var posts : ArrayList<Post>, var context : Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private val proxy : HttpProxyCacheServer = HttpProxyCacheServer(this.context)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var background = itemView.textureView
        var imgview = itemView.imageView
        var btnPlay = itemView.btnPlay
        var url : String? = null
        var mediaPlayer : MediaPlayer = MediaPlayer()
        init {
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.start()
            }
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.seekTo(0)
            }
            background.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                }
                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                }
                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    Log.d("onSurface","Destroyed $absoluteAdapterPosition")
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
                }, 50)
            }
            else mediaPlayer.start()
        }
        fun unSelected() {
            mediaPlayer.stop()
            mediaPlayer.prepareAsync()
        }
        fun pauseVideo() {
            mediaPlayer.stop()
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
        holder.poster.text = "trungbmt"
        holder.title.text = post.title
        try {
            holder.mediaPlayer.setDataSource(proxy.getProxyUrl(post.url))
            holder.mediaPlayer.prepareAsync()
        } catch (e: Exception) {}

        holder.mediaPlayer.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if(what==1) {
                    Glide.with(context)
                            .asDrawable()
                            .load(proxy.getProxyUrl(post.url))
                            .into(object : CustomTarget<Drawable>(){
                                override fun onLoadCleared(placeholder: Drawable?) {}
                                override fun onResourceReady(
                                        resource: Drawable,
                                        transition: Transition<in Drawable>?
                                ) {
                                    holder.background.visibility = View.GONE
                                    holder.imgview.background = resource
                                    holder.imgview.visibility = View.VISIBLE
                                }
                            })
                }
                return true
            }
        })
    }

}