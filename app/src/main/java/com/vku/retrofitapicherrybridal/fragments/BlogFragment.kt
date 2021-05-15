package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.PostAdapter
import com.vku.retrofitapicherrybridal.model.Post
import kotlinx.android.synthetic.main.fragment_blog.*
import kotlinx.android.synthetic.main.fragment_blog.view.*
import com.danikula.videocache.HttpProxyCacheServer
import java.lang.Exception


class BlogFragment : Fragment() {
    lateinit var rootView : View
    var curPosition = -1
    lateinit var viewpagerSearch: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_blog, container, false)

        var videos = ArrayList<Post>()
        videos.add(Post("Bro why you ruined a T-shirt ? It was so easy...", "https://v9-vn.tiktokcdn.com/639163ca9c32a1e88bc9a469dcca8336/60a00753/video/tos/alisg/tos-alisg-pve-0037/ac183128fdfd424fac0cc92c339a31e4/?a=1180&br=2124&bt=1062&cd=0%7C0%7C1&ch=0&cr=0&cs=0&cv=1&dr=3&ds=3&er=&l=202105151139210102340861603927EB5E&lr=tiktok&mime_type=video_mp4&net=0&pl=0&qs=0&rc=M3ltbHA7dmRpNTMzODgzM0ApNTdkNzVnZzw2Nzw8ZjU6NWdjaGxwaS1jYy9gLS1kLzRzczMtMTAvLy1fXl4yMTRiYDY6Yw%3D%3D&vl=&vr="))
        videos.add(Post("Give caption", "https://v16.tiktokcdn.com/c204235a4b57962b726b5a786e66bde9/60a03459/video/tos/alisg/tos-alisg-pve-0037/888b84fa6f824ce5b505a58809b4c7f0/?a=1180&br=2962&bt=1481&cd=0%7C0%7C1&ch=0&cr=0&cs=0&cv=1&dr=3&ds=3&er=&l=20210515145125010234109084533618B1&lr=tiktok&mime_type=video_mp4&net=0&pl=0&qs=0&rc=Mzh1O3hnNnRxNTMzODgzM0ApO2ZnZmU5aDxnN2c8OTZlaWdtcGVvbTJwYWNgLS1kLzRzcy4wYC5jNi1iYjU2NF9hYjM6Yw%3D%3D&vl=&vr="))
        videos.add(Post("Cute meow", "https://v16.tiktokcdn.com/02535f45cbc2ccb4b194264e00ee9c39/60a03458/video/tos/alisg/tos-alisg-pve-0037/7d3212c09c2d4772905d84d02c001333/?a=1180&br=3460&bt=1730&cd=0%7C0%7C1&ch=0&cr=0&cs=0&cv=1&dr=3&ds=3&er=&l=20210515145125010234109084533618B1&lr=tiktok&mime_type=video_mp4&net=0&pl=0&qs=0&rc=amRwcW5lbWlpNDMzOzgzM0ApZDk8MzlkZWU0N2U5ZDU6aGduZDVpM2ouMWNgLS0zLzRzcy4zXy0wLy0vLjZjY140NDQ6Yw%3D%3D&vl=&vr="))
        videos.add(Post("Death is like the wind, always by my side", "https://wallpapercave.com/wp/wp2757369.jpg"))
        videos.add(Post("chụp cho em cả buổi em chê mình ăn mặc như nhà quê", "https://v9-vn.tiktokcdn.com/679d1c41ad05140b8b03ea1f540e7929/60a03621/video/tos/alisg/tos-alisg-pve-0037/e66db9cc5f9c4aafa1089b48f35d5b2b/?a=1180&br=5240&bt=2620&cd=0%7C0%7C1&ch=0&cr=0&cs=0&cv=1&dr=3&ds=3&er=&l=2021051514585301011504101108366CE7&lr=tiktok&mime_type=video_mp4&net=0&pl=0&qs=0&rc=anc0czVrdXJwNTMzODgzM0ApNDNkODRpPDs3Nzg4MzRkaGdmcHM0aWpwYjRgLS1kLzRzczAuYWNeYGMyYy1eMmNfYjQ6Yw%3D%3D&vl=&vr="))

        rootView.viewpager2.adapter = PostAdapter(videos, this.context!!)
        viewpagerSearch = (rootView.viewpager2.get(0) as RecyclerView)
        rootView.viewpager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        rootView.viewpager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                (viewpagerSearch.findViewHolderForAdapterPosition(position) as PostAdapter.ViewHolder).selectedView(position)
                try {
                    (viewpagerSearch.findViewHolderForAdapterPosition(curPosition) as PostAdapter.ViewHolder).unSelected()
                } catch (e: Exception) {}

                if(curPosition!=position) {
                    try {
                        (viewpagerSearch.findViewHolderForAdapterPosition(curPosition) as PostAdapter.ViewHolder).btnPlay.visibility = View.GONE
                    } catch(e : Exception) {}
                }
                curPosition=position
                super.onPageSelected(position)
            }
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                try {
//                    if(curPosition==position) {
//                        (viewpagerSearch.findViewHolderForAdapterPosition(position+1) as PostAdapter.ViewHolder).preView()
//                    }
//                    else if(curPosition>position) {
//                        (viewpagerSearch.findViewHolderForAdapterPosition(position) as PostAdapter.ViewHolder).preView()
//                    }
//                } catch(e : Exception) {}
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
        })

        return rootView
    }
    fun pauseMusic() {
        (viewpagerSearch.findViewHolderForAdapterPosition(curPosition) as PostAdapter.ViewHolder).pauseVideo()
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            BlogFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}