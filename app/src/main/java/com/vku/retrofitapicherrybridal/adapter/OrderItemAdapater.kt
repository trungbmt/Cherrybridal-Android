package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.model.Cart
import kotlinx.android.synthetic.main.order_item_row.view.*

class OrderItemAdapater(var items : ArrayList<Cart>, var context : Context) : RecyclerView.Adapter<OrderItemAdapater.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.product_img
        var tvName = itemView.product_title
        var tvSize = itemView.order_size
        var tvAmount = itemView.order_num
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.order_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = items.get(position)
        holder.tvName.text = item.product.name
        holder.tvSize.text = item.detail.size
        holder.tvAmount.text = item.amount.toString()
        Glide.with(context)
            .load(AppConfig.IMAGE_URL+item.product.img)
            .centerCrop()
            .into(holder.img)
    }
}