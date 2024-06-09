package com.example.greenfresh.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.adapter.CheckoutAdapter
import com.example.greenfresh.adapter.OrderAdapter
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Cart
import com.example.greenfresh.model.Notification
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class OrderActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: OrderAdapter
    lateinit var list: ArrayList<Cart>

    lateinit var tvName: TextView
    lateinit var tv_phone_order: TextView
    lateinit var tv_address_order: TextView
    lateinit var tv_time_order: TextView
    lateinit var tv_status_order: TextView

    var uid = -1

    lateinit var order: Notification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        initView()
        uid = LoginApi().getIdUser(this)
        actionToolbar()
        list = ArrayList()

        getInformationUser()

        order = intent.getSerializableExtra("order") as Notification
        tv_address_order.text = order.address
        tv_time_order.text = "Order at: "+order.date
        getOrder(order)

    }

    private fun getInformationUser() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkUser
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                var name = ""
                var phone = ""
//                var address = ""
                if (response != null && response.length > 3) {
                    Log.d("getCart", "user : $response")
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        name = jsonObject.getString("name")
                        phone = jsonObject.getString("phone")
//                        address = jsonObject.getString("address")

                        tvName.text = name
                        tv_phone_order.text = phone


                    }
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

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_order)
        recyclerView = findViewById(R.id.recycle_view_order)


        tvName = findViewById(R.id.tv_name_order)
        tv_phone_order = findViewById(R.id.tv_phone_order)
        tv_address_order= findViewById(R.id.tv_address_order)
        tv_time_order= findViewById(R.id.tv_time_order)
        tv_status_order= findViewById(R.id.tv_status_order)
    }

    private fun actionToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getOrder(order: Notification) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkOrderDetail
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

                        list.add(Cart(id, name, thumb, price, num))

                    }
                    adapterPush(list)
                }
            }, { error ->
                Log.d("AAA", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["id_order"] = order.id.toString()
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

    private fun adapterPush(list: ArrayList<Cart>) {
        adapter = OrderAdapter(this, list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}