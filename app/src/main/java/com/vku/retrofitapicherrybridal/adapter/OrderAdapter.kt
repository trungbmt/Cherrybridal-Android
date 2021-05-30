package com.vku.retrofitapicherrybridal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.model.Order
import com.vku.retrofitapicherrybridal.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.order_row.view.*

class OrderAdapter(var orders : ArrayList<Order>, var context : Context, var orderViewModel: OrderViewModel) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime = itemView.order_time
        var rv = itemView.rv_items
        var tvNumProduct = itemView.total_order_product
        var tvPrice = itemView.total_order_price
        var tvStatus = itemView.order_status
        var tvFullName = itemView.odrer_infor_name
        var tvPhoneNum = itemView.odrer_infor_phonenum
        var tvAddress = itemView.order_infor_address
        var btnAbort = itemView.abort_order_btn
        init {
            rv.layoutManager = LinearLayoutManager(context)
            btnAbort.setOnClickListener {
                orderViewModel.abortOrder(orders.get(absoluteAdapterPosition).id, context, absoluteAdapterPosition)
                orders.get(absoluteAdapterPosition).status = -1
                notifyItemChanged(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var layoutinflater = LayoutInflater.from(parent.context)
        var view = layoutinflater.inflate(R.layout.order_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var order = orders.get(position)
        holder.tvTime.text = Tools.getDateTime(order.created_at)
        holder.rv.adapter = OrderItemAdapater2(order.items, context)
        holder.tvNumProduct.text = "${order.items.size} items"
        holder.tvPrice.text = Tools.format_currency(order.getPrice())
        holder.tvStatus.text = order.getStatus()
        holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, order.getIconId(), 0)
        holder.tvFullName.text = order.name
        holder.tvPhoneNum.text = order.phone
        holder.tvAddress.text = order.address

        if(order.status==0) {
            holder.btnAbort.visibility = View.VISIBLE
        } else {
            holder.btnAbort.visibility = View.GONE
        }
    }

}