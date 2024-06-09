package com.example.greenfresh.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.adapter.CheckoutAdapter
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Cart
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity() {
    lateinit var recyclerViewCheckout: RecyclerView
    lateinit var adapterCheckout: CheckoutAdapter
    lateinit var listPro: ArrayList<Cart>
    lateinit var toolbar: Toolbar
    lateinit var btn_payment: ConstraintLayout
    lateinit var btn_add_address: ImageView
    lateinit var tvAddress: TextView

    var uid: Int = -1
    lateinit var tv_amount: TextView
    lateinit var tv_ship: TextView
    lateinit var tv_voucher: TextView
    lateinit var tv_total: TextView

    companion object {
        var address: String = ""
    }

    var amount_price = 0.0
    var ship_price = 20
    var voucher_price = 15
    var total = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initView()
        actionToolbar()
        uid = LoginApi().getIdUser(this)
        getData()

        btn_add_address.setOnClickListener {
            showDialog()
        }

        btn_payment.setOnClickListener {
            if (address != "") {
                startActivity(Intent(this, PaymentActivity::class.java))
            } else {
                Toast.makeText(this, "Please enter address shipping", Toast.LENGTH_SHORT).show()
            }
        }
        if (address != "") {
            tvAddress.text = address

        }

    }

    private fun calculatePayment() {
        tv_amount.text = "$$amount_price"
        tv_ship.text = "$$ship_price"
        tv_voucher.text = "-$$voucher_price"
        tv_total.text =
            "$" + (amount_price + ship_price.toDouble() - voucher_price.toDouble()).toString()
    }

    private fun actionToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        recyclerViewCheckout = findViewById(R.id.recycleView_checkout)
        listPro = ArrayList()
        toolbar = findViewById(R.id.toolbar_edit_pro)
        btn_payment = findViewById(R.id.btn_save_pass)
        btn_add_address = findViewById(R.id.btn_add_address)
        tvAddress = findViewById(R.id.tv_address)

        tv_amount = findViewById(R.id.tv_amount)
        tv_ship = findViewById(R.id.tv_ship)
        tv_voucher = findViewById(R.id.tv_voucher)
        tv_total = findViewById(R.id.tv_total)
    }

    private fun getData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
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

                        listPro.add(Cart(id, name, thumb, price, num))
                        amount_price += price * num
                    }
                    adapterPush(listPro)
                    calculatePayment()
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
        adapterCheckout = CheckoutAdapter(this, list)
        recyclerViewCheckout.layoutManager = LinearLayoutManager(this)
        recyclerViewCheckout.adapter = adapterCheckout
    }


    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_address)
        val edtAddress: EditText = dialog.findViewById(R.id.edt_add_address)
        val btn_submit_address: TextView = dialog.findViewById(R.id.btn_submit_address)
        val btn_close_dialog: TextView = dialog.findViewById(R.id.btn_close_dialog)
        btn_close_dialog.setOnClickListener {
            dialog.dismiss()
        }
        btn_submit_address.setOnClickListener {

            tvAddress.text = address
            if (edtAddress.text.toString() != "") {
                address = edtAddress.text.toString()
                tvAddress.text = address
                edtAddress.setText(address)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show()
            }
        }


        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}