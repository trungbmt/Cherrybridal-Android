package com.vku.retrofitapicherrybridal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.model.Cart
import kotlinx.android.synthetic.main.order_item_row.view.*

class OrderItemAdapater(var items : ArrayList<Cart>) : RecyclerView.Adapter<OrderItemAdapater.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName = itemView.tvName
        var tvDetail = itemView.tvDetail
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
        holder.tvDetail.text = "(Size: ${item.detail.size}, x${item.amount})"
    }
}