package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.activities.DashboardActivity
import com.vku.retrofitapicherrybridal.fragments.CartFragment
import com.vku.retrofitapicherrybridal.model.Cart
import com.vku.retrofitapicherrybridal.viewmodel.CartViewModel
import kotlinx.android.synthetic.main.cart_row.view.*

class CartAdapter(var carts : ArrayList<Cart>, var context : Context, var cartViewModel: CartViewModel) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img = itemView.product_img
        var tvName = itemView.product_title
        var tvSize = itemView.size_name
        var tvAmount = itemView.product_quantity
        var tvPrice = itemView.product_price
        var tvDPrice = itemView.discount_price
        init {
            itemView.remove_item_btn.setOnClickListener {
                removeItem(absoluteAdapterPosition)
            }
        }
    }
    fun removeItem(position: Int) {
        cartViewModel.removeCart(carts.get(position).id)
        carts.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.cart_row, parent, false)


        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = carts.get(position)
        holder.tvName.text = cart.product.name
        holder.tvSize.text = cart.detail.size
        holder.tvAmount.text = cart.amount.toString()
        holder.tvPrice.text = Tools.format_currency((cart.amount*cart.detail.price))
        holder.tvDPrice.text = Tools.format_currency(((cart.amount*cart.detail.price)*1.3).toInt())
        Glide.with(context)
                .load(AppConfig.IMAGE_URL+cart.product.img)
                .centerCrop()
                .into(holder.img)
    }

}