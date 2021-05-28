package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.model.OrderItem
import kotlinx.android.synthetic.main.order_item_row.view.*

class OrderItemAdapater2(var items : ArrayList<OrderItem>, var context : Context) : RecyclerView.Adapter<OrderItemAdapater2.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.product_img
        var tvName = itemView.product_title
        var tvSize = itemView.order_size
        var tvAmount = itemView.order_num
        var tvPrice = itemView.item_price
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
        holder.tvAmount.text = item.quantity.toString()
        holder.tvPrice.text = Tools.format_currency(item.quantity*item.detail.price)
        Glide.with(context)
                .load(AppConfig.IMAGE_URL+item.product.img)
                .centerCrop()
                .into(holder.img)
    }

}