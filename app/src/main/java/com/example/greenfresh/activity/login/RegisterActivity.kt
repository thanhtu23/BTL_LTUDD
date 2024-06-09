package com.example.greenfresh.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.activity.MainActivity
import com.example.greenfresh.utils.CheckConnection
import com.example.greenfresh.utils.Server

class RegisterActivity : AppCompatActivity() {
    lateinit var btn_signIn: TextView
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtPhone: EditText
    lateinit var edtPass: EditText
    lateinit var edtPassConfirm: EditText
    lateinit var btnRegister: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()

        if (CheckConnection.isConnected(applicationContext)) {
            btnRegister.setOnClickListener {
                signUp()
            }

        } else {
            Toast.makeText(this, "No wifi", Toast.LENGTH_SHORT).show()
            finish()
        }

        btn_signIn.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

    }

    private fun signUp() {
        var name = edtName.text.toString()
        var email = edtEmail.text.toString().trim()
        var phone = edtPhone.text.toString().trim()
        var password = edtPass.text.toString()
        var password_confirm = edtPassConfirm.text.toString()

        if (name == "" && email == "" && phone == "" && password == "" && password_confirm == "") {
            Toast.makeText(this, "Please enter enough data", Toast.LENGTH_SHORT).show()
        } else {
            if (password == password_confirm) {
                var requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
                var link: String = Server.linkRegister
                var stringRequest =
                    object : StringRequest(Request.Method.POST, link,
                        Response.Listener {
                            if (it == "") {
                                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                            }
                        }, Response.ErrorListener {
                            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("AAA", it.toString())
                        }) {
                        @Throws(AuthFailureError::class)
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["name"] = name
                            params["email"] = email
                            params["phone"] = phone
                            params["pass"] = password
                            Log.d("FFF", "getParams: $name , $email, $phone, $password")
                            return params
                        }
                    }
                requestQueue.add(stringRequest)

            } else {
                Toast.makeText(this, "Wrong confirm password", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initView() {
        btn_signIn = findViewById(R.id.tv_sign_in_btn)
        edtName = findViewById(R.id.edt_name_register)
        edtEmail = findViewById(R.id.edt_email_register)
        edtPhone = findViewById(R.id.edt_phone_register)
        edtPass = findViewById(R.id.edt_pass_register)
        edtPassConfirm = findViewById(R.id.edt_confirm_pass_register)

        btnRegister = findViewById(R.id.btn_register)
    }
}