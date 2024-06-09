package com.example.greenfresh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenfresh.R

class VoucherAdapter(var listVoucher: ArrayList<String>) :
    RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title_voucher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_voucher, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text= listVoucher[position]
    }

    override fun getItemCount(): Int {
        return listVoucher.size
    }


}