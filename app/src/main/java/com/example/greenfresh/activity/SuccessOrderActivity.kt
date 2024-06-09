package com.example.greenfresh.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Notification
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class SuccessOrderActivity : AppCompatActivity() {
    lateinit var tv_continue_shop: TextView
    lateinit var tv_view_order : TextView

    var id_order = -1
    var uid = -1
    companion object {
        lateinit var order : Notification
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        tv_continue_shop = findViewById(R.id.tv_continue_shop)
        tv_view_order = findViewById(R.id.tv_view_order)

        uid = LoginApi().getIdUser(this)
        id_order = intent.getIntExtra("id_order",-1)
        getOrder(id_order)

        tv_view_order.setOnClickListener {
            val i = Intent(this,OrderActivity::class.java)
            i.putExtra("order", order)
            startActivity(i)
        }
        tv_continue_shop.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    private fun getOrder(idOrder: Int) {
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

                        order=(Notification(id, date,payment, status, address))
//                        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
                    }

                }
            }, { error ->
                Log.d("AAA", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["id_order"] = idOrder.toString()
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }
}