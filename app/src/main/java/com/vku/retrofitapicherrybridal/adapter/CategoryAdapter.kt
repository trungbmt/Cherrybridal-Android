package com.vku.retrofitapicherrybridal.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.activities.DashboardActivity
import com.vku.retrofitapicherrybridal.fragments.ShopFragment
import com.vku.retrofitapicherrybridal.fragments.ShopProductFragment
import com.vku.retrofitapicherrybridal.model.Category
import kotlinx.android.synthetic.main.category_item.view.*

class CategoryAdapter(var categories : List<Category>, var context : Context) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                var bundle = Bundle()
                var category = categories.get(absoluteAdapterPosition).id
                bundle.putInt("category_id", category)
                var shopProductFragment = ShopProductFragment()
                shopProductFragment.arguments = bundle
                (context as DashboardActivity).replaceOtherFragment(shopProductFragment)
            }
        }
        var img : ImageView = itemView.category_item_img
        var name : TextView = itemView.category_item_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.category_item, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories.get(position)
        holder.name.text = category.name
        Log.d("IMAGEURL", AppConfig.IMAGE_URL+category.img)
        Glide
            .with(context)
            .load(AppConfig.IMAGE_URL+category.img)
            .centerCrop()
            .placeholder(R.drawable.loading)
            .into(holder.img)
    }

}