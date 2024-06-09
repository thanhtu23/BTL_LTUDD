package com.example.greenfresh.activity.login

import android.content.Intent
import android.content.SharedPreferences
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
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.utils.CheckConnection
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var btn_login: ConstraintLayout
    lateinit var btn_sign_up: TextView
    lateinit var edtEmail: EditText
    lateinit var edtPass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
        edtEmail.setText("tu@gmail.com")
        edtPass.setText("123456")
        if (CheckConnection.isConnected(applicationContext)) {
            btn_login.setOnClickListener {
                logIn()
            }

        } else {
            Toast.makeText(this, "No wifi", Toast.LENGTH_SHORT).show()
            finish()
        }
        btn_sign_up.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }

    }

    private fun initView() {
        btn_sign_up = findViewById(R.id.tv_signup_btn)
        btn_login = findViewById(R.id.btn_login)
        edtEmail = findViewById(R.id.edt_email_login)
        edtPass = findViewById(R.id.edtPassword_login)
    }

    private fun logIn() {
        var email = edtEmail.text.toString()
        var pass = edtPass.text.toString()
        if (email == "" || pass == "") {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
        } else {
            var requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
            var link: String = Server.linkLogin
            var stringRequest =
                object : StringRequest(Request.Method.POST, link,
                    Response.Listener {
                        if (it.length >= 0) {
                            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                            LoginApi().saveIdUser(this,it.toInt())// save id user

                            val i = Intent(this, MainActivity::class.java)
                            i.putExtra("email", email)
                            startActivity(i)
                        } else if (it == "Fail") {
                            Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Log.d("Login", "logIn: $it")
                            Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("AAA", it.toString())
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["email"] = email
                        params["pass"] = pass
                        Log.d("AAAA", "getParams: $email , $pass")
                        return params
                    }
                }
            requestQueue.add(stringRequest)
        }
    }


}