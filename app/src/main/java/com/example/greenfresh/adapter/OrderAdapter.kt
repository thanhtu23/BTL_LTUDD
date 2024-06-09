package com.example.greenfresh.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.model.Cart

class OrderAdapter(var context: Context, private var cartList: ArrayList<Cart>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate: View =
            LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = cartList.get(position).name
        holder.tvPrice.text = "$" + (cartList.get(position).price * cartList.get(position).num).toString()
        Glide.with(holder.itemView.context).load(cartList.get(position).thumb).into(holder.pic)
        holder.tvNumber.text = "Qty= "+cartList.get(position).num.toString()
    }
    override fun getItemCount(): Int {
        return cartList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tv_itemCart_title)
        val pic: ImageView = itemView.findViewById(R.id.picCart)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_itemCart_price)
        val tvNumber: TextView = itemView.findViewById(R.id.tv_number)

    }





}