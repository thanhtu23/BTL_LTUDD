package com.example.greenfresh.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.model.Payment

class PaymentAdapter(var context: Context, var list: ArrayList<Payment>) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {
    companion object {
        var checkedPosition = -1
        var name_payment =""
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name_pay)
        val img: ImageView = itemView.findViewById(R.id.img_pay)
        val check: RadioButton = itemView.findViewById(R.id.radio_check_pay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list.get(position).name
        Glide.with(context).load(list.get(position).image).into(holder.img)
//        name_payment = list.get(position).name
        holder.check.isChecked = checkedPosition == position
        holder.check.setOnClickListener {
            checkedPosition = position
            name_payment = list.get(checkedPosition).name
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}