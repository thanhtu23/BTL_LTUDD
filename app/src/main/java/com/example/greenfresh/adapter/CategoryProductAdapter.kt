package com.example.greenfresh.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.greenfresh.R
import com.example.greenfresh.model.Category

class CategoryProductAdapter(var cateList: ArrayList<Category>, val onItemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>() {
    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cate_product, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = cateList.get(position).name

        if (selectedPosition == position) {
            val colorDrawable = ColorDrawable(Color.GREEN)
            holder.tvTitle.setBackgroundResource(R.drawable.bg_cate_pro_2)
            holder.tvTitle.setTextColor(Color.WHITE)
        } else { // Nếu không được chọn, thì set lại màu mặc định

            holder.tvTitle.setBackgroundResource(R.drawable.bg_cate_pro)
            holder.tvTitle.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {

            onItemClick(cateList[position])

            val colorDrawable = ColorDrawable(Color.GREEN)
            holder.tvTitle.background = colorDrawable
            holder.tvTitle.setTextColor(Color.WHITE)
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int {
        return cateList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_cate_pro)
    }
}