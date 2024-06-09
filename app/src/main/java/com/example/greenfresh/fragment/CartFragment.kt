package com.example.greenfresh.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.activity.CheckoutActivity
import com.example.greenfresh.adapter.CartAdapter
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Cart
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class CartFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var cartList: ArrayList<Cart>
    lateinit var adapterCart: CartAdapter
    lateinit var tvTotal: TextView
    lateinit var recyclerViewCart: RecyclerView
    lateinit var refresh: SwipeRefreshLayout

    lateinit var btn_checkout: ConstraintLayout
    var uid = -1;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerViewCart = view.findViewById(R.id.recycle_cart)
        tvTotal = view.findViewById(R.id.tvTotalAll)
        refresh = view.findViewById(R.id.refreshLayout)
        btn_checkout = view.findViewById(R.id.btn_save_pass)

        cartList = ArrayList()
        uid = LoginApi().getIdUser(requireContext())
        Log.i("tb",uid.toString())
        refresh.setOnRefreshListener(this)
        refresh.post {
            cartList.clear()
            getData()

        }

        btn_checkout.setOnClickListener {
            startActivity(Intent(requireContext(),CheckoutActivity::class.java))
        }
        return view
    }

    private fun getData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val link: String = Server.linkGetCart
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                var id = 0
                var name = ""
                var thumb = ""
                var price = 0.0
                var num = 0
                if (response != null && response.length != 2) {
                    Log.d("getCart", "getCart: $response")
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        id = jsonObject.getInt("id")
                        name = jsonObject.getString("name")
                        thumb = jsonObject.getString("thumb")
                        price = jsonObject.getDouble("price")
                        num = jsonObject.getInt("num")

                        cartList.add(Cart(id, name, thumb, price, num))
                        Log.d("FFF", "getCart: $name")
                    }
                    adapterPush(cartList)
                    refresh.isRefreshing = false
                    getTotalCart()
                }
            }, { error ->
                Log.d("AAA", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

    private fun adapterPush(list: ArrayList<Cart>) {
        adapterCart = CartAdapter(requireContext(), list)
        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCart.adapter = adapterCart
    }

    fun getTotalCart() {
        var total = 0.0
        for (i in cartList) {
            total += i.price * i.num
        }
        tvTotal.text = "$$total"
    }

    override fun onRefresh() {
        cartList.clear()
        getData()
    }
}
