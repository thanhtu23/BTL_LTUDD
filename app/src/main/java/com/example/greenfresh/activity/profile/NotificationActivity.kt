package com.example.greenfresh.activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.adapter.NotificationAdapter
import com.example.greenfresh.adapter.OrderAdapter
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Cart
import com.example.greenfresh.model.Notification
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class NotificationActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerViewNoti: RecyclerView
    lateinit var adapter: NotificationAdapter
    lateinit var listNoti: ArrayList<Notification>
    var uid = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        initView()
        actionToolbar()
        uid = LoginApi().getIdUser(this)
        getNotification()
    }

    private fun getNotification() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkNotifi
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                var id = 0
                var date = ""
                var payment =""
                var status = ""
                var address = ""
                if (response != null && response.length != 2) {
                    Log.d("getCart", "getCart: $response")
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        id = jsonObject.getInt("id")
                        date = jsonObject.getString("date")
                        payment = jsonObject.getString("payment")
                        address = jsonObject.getString("address_ship")
                        status = jsonObject.getString("status")

                        listNoti.add(Notification(id, date,payment, status, address))

                    }
                    adapterPush(listNoti)
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

    private fun adapterPush(list: ArrayList<Notification>) {
        adapter = NotificationAdapter(this, list)
        recyclerViewNoti.layoutManager = LinearLayoutManager(this)
        recyclerViewNoti.adapter = adapter
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_notification)
        listNoti = ArrayList()
        recyclerViewNoti = findViewById(R.id.recycle_view_notification)
        adapter = NotificationAdapter(this, listNoti)
    }

    private fun actionToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


}