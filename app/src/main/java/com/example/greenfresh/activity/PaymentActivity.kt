package com.example.greenfresh.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.adapter.PaymentAdapter
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Payment
import com.example.greenfresh.utils.Server

class PaymentActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PaymentAdapter
    lateinit var list : ArrayList<Payment>
    lateinit var toolbar: Toolbar
    lateinit var btnConfirm : ConstraintLayout

    var uid = -1
    companion object {
        var id_order = -1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        initView()
        uid = LoginApi().getIdUser(this)
        getPayment()
        actionToolbar()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnConfirm.setOnClickListener {
            if(PaymentAdapter.checkedPosition >-1){
                insertOrder()

                val i  = Intent(this, SuccessOrderActivity::class.java)
                i.putExtra("id_order",id_order)
                startActivity(i)

            }
            else {
                Toast.makeText(this, "Please choose payment methods", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertOrder() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkOrder
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                id_order = response.toInt()
            }, { error ->
                Log.d("Error: ", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["address"] = CheckoutActivity.address
                    params["payment"] = PaymentAdapter.name_payment
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

    private fun actionToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getPayment() {
        list.add(Payment("Paypal","data:image/png;base64,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"))
        list.add(Payment("Google Pay","https://upload.wikimedia.org/wikipedia/commons/thumb/5/53/Google_%22G%22_Logo.svg/2008px-Google_%22G%22_Logo.svg.png"))
        list.add(Payment("Visa","https://cdn-icons-png.flaticon.com/512/217/217425.png"))
        list.add(Payment("Master Card","https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Mastercard-logo.svg/1280px-Mastercard-logo.svg.png"))
//        list.add(Payment("Google Pay",""))
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycle_payment)
        list = ArrayList()
        adapter = PaymentAdapter(this,list)
        toolbar = findViewById(R.id.toolbar_payment)
        btnConfirm =findViewById(R.id.btn_save_pass)
    }
}