package com.example.greenfresh.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar

    lateinit var edtName : EditText
    lateinit var edtAddress : EditText
    lateinit var edtPhone : EditText
    lateinit var edtEmail :TextView
    lateinit var btnUpdate :ConstraintLayout
    var uid = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initView()
        actionToolBar()
        uid = LoginApi().getIdUser(this)
        getData()

        btnUpdate.setOnClickListener {
            updateData()
        }
    }

    

    private fun actionToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_edit_pro)
        edtName = findViewById(R.id.edt_name_per)
        edtAddress = findViewById(R.id.edt_address_per)
        edtPhone = findViewById(R.id.edt_phone_per)
        edtEmail = findViewById(R.id.edt_email_per)
        edtEmail.setOnClickListener {
            Toast.makeText(this, "You can't change email!", Toast.LENGTH_SHORT).show()
        }
        btnUpdate = findViewById(R.id.btn_update)
    }

    private fun getData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkUser
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->

                var name = ""
                var phone = ""
                var address = ""
                var email = ""

                if (response != null && response.length > 3) {
                    Log.d("getCart", "user : $response")
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        name = jsonObject.getString("name")
                        phone = jsonObject.getString("phone")
                        address = jsonObject.getString("address")
                        email = jsonObject.getString("email")

                        edtName.setText(name)
                        edtPhone.setText(phone)
                        edtEmail.setText(email)
                        edtAddress.setText(address)

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
    private fun updateData() {
        var name = edtName.text
        var phone = edtPhone.text
        var address = edtAddress.text
        var email = edtEmail.text

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkUser
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,ProfileActivity::class.java))
                if (response != null && response.length > 3) {
                    
                }
            }, { error ->
                Log.d("AAA", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["name"] = name.toString()
                    params["phone"] = phone.toString()
                    params["address"] = address.toString()
                    params["email"] = email.toString()
                    return params
                }
            }
        requestQueue.add(stringRequest)

    }
}