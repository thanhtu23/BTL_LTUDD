package com.example.greenfresh.activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.R
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.utils.Server
import org.json.JSONArray
import org.json.JSONObject

class SecurityActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var edtPass: EditText
    lateinit var edtNewPass: EditText
    lateinit var edtConfPass: EditText
    lateinit var btn_save : ConstraintLayout
    var uid = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        initView()
        uid = LoginApi().getIdUser(this)

        actionToolbar()

        btn_save.setOnClickListener {
            changePass()
        }

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
        toolbar = findViewById(R.id.toolbar_security)
        edtConfPass = findViewById(R.id.edt_conf_pass)
        edtNewPass = findViewById(R.id.edt_new_pass)
        edtPass = findViewById(R.id.edt_current_pass)
        btn_save = findViewById(R.id.btn_save_pass)

    }

    private fun getPass(callback: (String) -> Unit){
        var pass = ""
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkChangePass
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->

                if (response != null && response.isNotEmpty()) {
                        pass = response
                        callback(pass)
                    
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

    private fun changePass() {

        var pass = edtPass.text.toString()
        var newPass = edtNewPass.text.toString()
        var confPass = edtConfPass.text.toString()
        getPass {
            if (pass ==it && newPass==confPass){
                val requestQueue: RequestQueue = Volley.newRequestQueue(this)
                val link: String = Server.linkChangePass
                val stringRequest =
                    object : StringRequest(Method.POST, link, { response ->
                        Log.d("DATA", response)
                    }, { error ->
                        Log.d("AAA", error.toString())
                    }) {
                        @Throws(AuthFailureError::class)
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["uid"] = uid.toString()
                            params["pass"] = newPass
                            return params
                        }
                    }
                requestQueue.add(stringRequest)
                Toast.makeText(this, "Change password successful", Toast.LENGTH_SHORT).show()
                edtPass.setText("")
                edtNewPass.setText("")
                edtConfPass.setText("")


            } else if(pass !=it ){
                edtPass.error = "Wrong Password"
                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
            } else if(newPass !=confPass){
                edtConfPass.error = "Wrong confirm password"
                Toast.makeText(this, "Wrong confirm password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}