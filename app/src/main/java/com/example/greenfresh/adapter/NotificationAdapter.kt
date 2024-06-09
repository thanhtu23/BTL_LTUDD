package com.example.greenfresh.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenfresh.R
import com.example.greenfresh.activity.OrderActivity
import com.example.greenfresh.model.Notification

class NotificationAdapter(var context: Context, private var list: ArrayList<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate: View =
            LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOrder.text = "Order successful #id: "+list.get(position).id
        holder.tv_noti_date.text = list.get(position).date

        holder.btn_noti_view_detail.setOnClickListener {
            val i  = Intent(context, OrderActivity::class.java)
            i.putExtra("order",list.get(position))
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvOrder: TextView = itemView.findViewById(R.id.tv_notifi_id)
        val tv_noti_date: TextView = itemView.findViewById(R.id.tv_noti_date)
        val btn_noti_view_detail: TextView = itemView.findViewById(R.id.btn_noti_view_detail)

    }
}