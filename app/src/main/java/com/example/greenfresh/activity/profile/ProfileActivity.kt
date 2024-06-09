package com.example.greenfresh.activity.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.greenfresh.R
import com.example.greenfresh.activity.CartActivity
import com.example.greenfresh.activity.MainActivity
import com.example.greenfresh.activity.ProductActivity
import com.example.greenfresh.activity.login.LoginActivity
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.utils.Server
import com.github.ybq.android.spinkit.SpinKitView
import org.json.JSONArray
import org.json.JSONObject


class ProfileActivity : AppCompatActivity() {
    lateinit var picAvt: ImageView
    lateinit var tvName: TextView
    lateinit var btnEdit: LinearLayout
    lateinit var btnSecurity: LinearLayout
    lateinit var btn_upload: ImageView
    lateinit var btn_help_center  : LinearLayout
    lateinit var imagePath : Uri
    lateinit var tvEmail : TextView
    lateinit var btn_notification : LinearLayout
    lateinit var btn_logout: LinearLayout
    // upload
    lateinit var img_upload : ImageView
    lateinit var tv_progress : TextView
    lateinit var secureUrl : String
    lateinit var spinKitView_upload : SpinKitView
    companion object {
        private var isInitMedia = false
    }
    var uid: Int = -1
    private val IMAGE_REQ = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initView()
        initConfig()
        uid = LoginApi().getIdUser(this)
        getData()


        btnEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        btn_upload.setOnClickListener {
            dialogUploadImage()
        }
        btnSecurity.setOnClickListener {
            startActivity(Intent(this, SecurityActivity::class.java))
        }
        btn_help_center.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }
        btn_notification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        btn_logout.setOnClickListener {
            showDialog()
        }
        bottomNavagation()
    }

    fun showDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)

        // handle in here
        val btn_canel: TextView = dialog.findViewById(R.id.btn_cancel_dialog)
        val btn_yes: TextView = dialog.findViewById(R.id.btn_yes_dialog)

        btn_canel.setOnClickListener {
            dialog.dismiss()
        }
        btn_yes.setOnClickListener {
            logout()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
    private fun logout(){
        LoginApi().clearIdUser(this)
        startActivity(Intent(applicationContext,LoginActivity::class.java))
    }
    private fun dialogUploadImage() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_change_image)

        val close: ImageButton = dialog.findViewById(R.id.btn_close)
        val btnUpload : TextView = dialog.findViewById(R.id.btn_upload)
        tv_progress=dialog.findViewById(R.id.tv_progress)
        img_upload = dialog.findViewById(R.id.img_upload)
        spinKitView_upload = dialog.findViewById(R.id.spinKitView_upload)
        spinKitView_upload.visibility = View.GONE
        btnUpload.setOnClickListener {
            uploadImageToServer(dialog)
        }


        close.setOnClickListener {
            dialog.dismiss()
        }
        img_upload.setOnClickListener {
            requestPermission()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun uploadImageToServer(dialog: Dialog) {
        MediaManager.get().upload(imagePath).callback(object : UploadCallback {
            override fun onStart(requestId: String?) {
                tv_progress.text = "Start Upload"

            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                tv_progress.text = "Uploading"
                spinKitView_upload.visibility = View.VISIBLE

            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                secureUrl = resultData?.get("secure_url").toString()

                tv_progress.text = "Upload Success"
                spinKitView_upload.visibility = View.GONE

                insertImageToDatabase(secureUrl)
                Glide.with(applicationContext).load(secureUrl).into(picAvt)

                Handler(Looper.getMainLooper()).postDelayed({
                    dialog.dismiss()
                },2000)

            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                tv_progress.text = "Upload Fail: ${error.toString()}"
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                tv_progress.text = "Upload Fail: ${error.toString()}"
            }

        }).dispatch()
    }

    private fun insertImageToDatabase(secureUrl: String) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkUser
        val stringRequest =
            object : StringRequest(Method.POST, link, {

            }, { error ->
                Log.d("AAA", error.toString())
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["url"] = secureUrl
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

    private fun requestPermission() {
        selectImage()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is granted, do something
            selectImage()

        } else {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                IMAGE_REQ
            )
        }
    }

    private fun selectImage() {
        val i = Intent()
        i.type = "image/*"
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(i, IMAGE_REQ)
    }

    private fun initConfig() {
        if(!isInitMedia){
            val config = HashMap<String, Any>()
            config["cloud_name"] = "dxzr2klk5"
            config["api_key"] = "811516443349719"
            config["api_secret"] = "vLraKvlm1Ru-OVGXC_OHv_j2siI"
            MediaManager.init(this, config)
            isInitMedia = true
        }

    }

    private fun initView() {
        picAvt = findViewById(R.id.avt_profile)
        tvName = findViewById(R.id.name_profile)
        btnEdit = findViewById(R.id.btn_phone)
        btnSecurity = findViewById(R.id.btn_security)
        btn_upload = findViewById(R.id.img_upload_avt)
        tvEmail = findViewById(R.id.tv_email_profile)
        btn_help_center = findViewById(R.id.btn_help_center)
        btn_notification = findViewById(R.id.btn_notification)
        btn_logout = findViewById(R.id.btn_logout)
    }

    private fun getData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkUser
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                var name = ""
                var avt = ""
                var mail = ""
                if (response != null && response.length > 3) {
                    Log.d("getCart", "user : $response")
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        name = jsonObject.getString("name")
                        avt = jsonObject.getString("avt")
                        mail = jsonObject.getString("email")
                        tvEmail.text = mail
                        tvName.text = name
                        Glide.with(this).load(avt).into(picAvt)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imagePath = data.data!!
            Glide.with(this).load(imagePath).into(img_upload)

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun bottomNavagation() {

        val drawable = resources.getDrawable(R.drawable.ic_baseline_person, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColorFilter(resources.getColor(R.color.green, null), PorterDuff.Mode.SRC_IN)
        }

        val img: ImageView = findViewById(R.id.imgView_person)
        val text: TextView = findViewById(R.id.tvSetting)
        img.setImageDrawable(drawable)
        text.setTextColor(ContextCompat.getColor(this, R.color.green))

        val btnHome: LinearLayout = findViewById(R.id.btn_home_bottom)
        val btnDiscover: LinearLayout = findViewById(R.id.btn_discover_bottom)
        val btnCart: LinearLayout = findViewById(R.id.btn_cart_bottom)
        var btnProfile: LinearLayout = findViewById(R.id.btn_profile_bottom)
        btnHome.setOnClickListener {

            startActivity(Intent(applicationContext, MainActivity::class.java))

        }
        btnDiscover.setOnClickListener {
            startActivity(Intent(applicationContext, ProductActivity::class.java))

        }
        btnCart.setOnClickListener {
            startActivity(Intent(applicationContext, CartActivity::class.java))
        }
//        btnProfile.setOnClickListener {
//            startActivity(Intent(applicationContext, ProfileActivity::class.java))
//        }
    }
}