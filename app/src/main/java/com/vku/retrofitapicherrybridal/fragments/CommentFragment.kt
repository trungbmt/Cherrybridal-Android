package com.vku.retrofitapicherrybridal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.adapter.CommentAdapter
import com.vku.retrofitapicherrybridal.model.PostComment
import com.vku.retrofitapicherrybridal.viewmodel.CommentViewModel
import kotlinx.android.synthetic.main.fragment_comment.view.*


class CommentFragment(var postId : Int) : BottomSheetDialogFragment() {
    lateinit var rootView : View
    lateinit var commentViewModel : CommentViewModel
    var mCommentCountLiveData = MutableLiveData<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_comment, container, false)
        commentViewModel =  ViewModelProvider(this).get(CommentViewModel::class.java)
        commentViewModel.getComments(postId)
        var commentAdapter = CommentAdapter(ArrayList<PostComment>(), requireContext())
        commentViewModel.mCommentsLiveData.observe(viewLifecycleOwner, Observer {
            commentAdapter = CommentAdapter(it, context!!)
            rootView.rv_comment.adapter = commentAdapter
            rootView.progress_bar.visibility = View.GONE
            rootView.commentCount.text = "${it.size} comments"
            mCommentCountLiveData.value = it.size
        })
        commentViewModel.mNewComment.observe(viewLifecycleOwner, Observer {
            commentAdapter.comments.add(0, it)
            commentAdapter.notifyItemInserted(0)
        })
        rootView.rv_comment.layoutManager = LinearLayoutManager(this.context)
        rootView.rv_comment.setHasFixedSize(true)
        rootView.btnSubmit.setOnClickListener {
            postComment()
        }


        return rootView
    }
    fun postComment() {
        val content = rootView.editText.text.toString()

        if(content.length>0) {
            rootView.editText.setText("")
            var options = HashMap<String, String>()
            options.put("content", content)
            commentViewModel.postComment(postId, options)
        }
    }
    fun getComments() {

    }
}