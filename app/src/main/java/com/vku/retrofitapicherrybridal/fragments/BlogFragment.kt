package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.PostAdapter
import kotlinx.android.synthetic.main.fragment_blog.view.*
import com.vku.retrofitapicherrybridal.viewmodel.BlogViewModel
import java.lang.Exception


class BlogFragment : Fragment() {
    lateinit var rootView : View
    var curPosition = -1
    lateinit var viewpagerSearch: RecyclerView
    private lateinit var blogViewModel : BlogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_blog, container, false)

        blogViewModel = ViewModelProvider(this).get(BlogViewModel::class.java)
        blogViewModel.getPostsLiveData().observe(this, Observer {
            var categoryAdapter = PostAdapter(it, this.context!!)
            rootView.viewpager2.adapter  = categoryAdapter
        })

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
        })

        return rootView
    }
    fun pauseMusic() {
        try {

            (viewpagerSearch.findViewHolderForAdapterPosition(curPosition) as PostAdapter.ViewHolder).pauseVideo()
        } catch (e: Exception) {}
    }
    fun getPosts() {

    }
    companion object {
        @JvmStatic
        fun newInstance() =
            BlogFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}