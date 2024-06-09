package com.example.greenfresh.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Cart
import com.example.greenfresh.utils.Server

class CartAdapter(var context: Context, private var cartList: ArrayList<Cart>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var num = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate: View =
            LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        num = cartList.get(position).num

        holder.tvName.text = cartList.get(position).name
        holder.tvPrice.text = "$" + (cartList.get(position).price * num).toString()

        Glide.with(holder.itemView.context).load(cartList.get(position).thumb).into(holder.pic)

        holder.minus.setOnClickListener {

            num--
            if (num < 1) {
                num = 1
            }

            holder.tvNumber.text = num.toString()
            holder.tvPrice.text = "$" + (cartList.get(position).price * num).toString()

            var uid = LoginApi().getIdUser(context)
            var idPro =cartList.get(position).idPro
            Submit("PUT_NUMBER",num.toString(),null,uid,idPro,position)
        }
        holder.plus.setOnClickListener {
            num++
            holder.tvNumber.text = num.toString()
            holder.tvPrice.text = "$" + (cartList.get(position).price * num).toString()

            var uid = LoginApi().getIdUser(context)
            var idPro =cartList.get(position).idPro
            Submit("PUT_NUMBER",num.toString(),null,uid,idPro,position)
        }
        holder.tvNumber.text = cartList.get(position).num.toString()

        holder.delete.setOnClickListener {
            var uid = LoginApi().getIdUser(context)
            var idPro =cartList.get(position).idPro
            deleteItemCart(uid,idPro , position)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tv_itemCart_title)
        val pic: ImageView = itemView.findViewById(R.id.picCart)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_itemCart_price)
        val minus: TextView = itemView.findViewById(R.id.minus)
        val plus: TextView = itemView.findViewById(R.id.plus)
        val tvNumber: TextView = itemView.findViewById(R.id.tv_number)
        val delete: ImageView = itemView.findViewById(R.id.pic_delete)
    }

    private fun deleteItemCart(uid: Int, idPro: Int, position: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_delete_item_cart)

        // handle in here
        val btn_canel: TextView = dialog.findViewById(R.id.btn_cancel_dialog)
        val btn_yes: TextView = dialog.findViewById(R.id.btn_yes_dialog)

        val title: TextView = dialog.findViewById(R.id.tv_itemCart_title)
        val pic: ImageView = dialog.findViewById(R.id.picCart)
        val price: TextView = dialog.findViewById(R.id.tv_itemCart_price)

        //set
        title.text = cartList.get(position).name
        Glide.with(dialog.context).load(cartList.get(position).thumb).into(pic)
        price.text = "$" + cartList.get(position).price

        btn_canel.setOnClickListener {
            dialog.dismiss()
        }
        btn_yes.setOnClickListener {
            Submit("DELETE","",dialog,uid,idPro,position)
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun Submit(method: String, data: String, dialog: Dialog?, uid: Int, idPro: Int, position: Int) {
        if(method=="DELETE"){
            val request: StringRequest = object : StringRequest(
                Method.POST, Server.linkGetCart,
                { response ->
                    dialog!!.dismiss()
                    cartList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    Toast.makeText(context, "Fail $error", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id_del"] = idPro.toString()
                    params["uid"] = uid.toString()
                    return params
                }
            }
            Volley.newRequestQueue(context).add(request)
        } else if(method == "PUT_NUMBER"){
            val request: StringRequest = object : StringRequest(
                Method.POST, Server.linkUpdateNumCart,
                { response ->
//                    dialog.dismiss()
//                    cartList.get(position).num = data.toInt()
//                    notifyItemChanged(position)

                },
                { error ->
                    Toast.makeText(context, "Fail $error", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["id_up"] = idPro.toString()
                    params["num"] = data
                    return params
                }
            }
            Volley.newRequestQueue(context).add(request)
        }
    }

}
