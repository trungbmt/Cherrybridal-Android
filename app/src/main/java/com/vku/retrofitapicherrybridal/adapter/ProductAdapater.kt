package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.activities.DashboardActivity
import com.vku.retrofitapicherrybridal.fragments.ProductShowFragment
import com.vku.retrofitapicherrybridal.fragments.ShopProductFragment
import com.vku.retrofitapicherrybridal.model.Product
import kotlinx.android.synthetic.main.product_row.view.*

class ProductAdapater(var products : ArrayList<Product>, var context : Context) : RecyclerView.Adapter<ProductAdapater.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.img
        val tvName = itemView.tvName
        val tvPrice  = itemView.tvPrice
        init {
            itemView.setOnClickListener {
                var bundle = Bundle()
                var product = products.get(absoluteAdapterPosition).id
                bundle.putInt("product_id", product)
                var productShowFragment = ProductShowFragment()
                productShowFragment.arguments = bundle
                (context as DashboardActivity).replaceOtherFragment(productShowFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.product_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products.get(position)
        holder.tvName.text = product.name
        holder.tvPrice.text = Tools.format_currency(product.price)
        Glide
                .with(context)
                .load(product.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(holder.img)
    }
}