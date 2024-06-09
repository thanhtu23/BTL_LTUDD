package com.example.greenfresh.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.activity.profile.ProfileActivity
import com.example.greenfresh.adapter.BestSellerAdapter
import com.example.greenfresh.adapter.CategoryAdapter
import com.example.greenfresh.adapter.VoucherAdapter
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Category
import com.example.greenfresh.model.Product
import com.example.greenfresh.utils.CheckConnection
import com.example.greenfresh.utils.Server
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var adapterCate: CategoryAdapter
    lateinit var adapterBestSeller: BestSellerAdapter
    lateinit var adapterVoucher: VoucherAdapter
    lateinit var arrVoucher: ArrayList<String>
    lateinit var viewFlipper: ViewFlipper
    lateinit var recycleViewCate: RecyclerView
    lateinit var recyclerViewSeller: RecyclerView
    lateinit var recyclerViewVoucher: RecyclerView
    lateinit var arrBanner: ArrayList<String>

    lateinit var imgAvt : CircleImageView

    var uid : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()


        actionViewFlipper()
        recyclerViewCategory()
        recyclerViewBestSeller()
        recyclerViewVoucherAction()
        getDataUser()
        bottomNavagation()
        if (CheckConnection.isConnected(applicationContext)) {

            uid = LoginApi().getIdUser(applicationContext)



        } else {
            Toast.makeText(this, "No wifi", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun bottomNavagation() {

        val drawable = resources.getDrawable(R.drawable.ic_baseline_home_grey, null)
// Set the color filter on the drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColorFilter(resources.getColor(R.color.green, null), PorterDuff.Mode.SRC_IN)
        }
// Set the drawable to an ImageView or other view
//        imageView.setImageDrawable(drawable)

        val img : ImageView = findViewById(R.id.imgViewHome)
        val text : TextView = findViewById(R.id.tvHome)
        img.setImageDrawable(drawable)
        text.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))

        val btnHome: LinearLayout=findViewById(R.id.btn_home_bottom)
        val btnDiscover: LinearLayout = findViewById(R.id.btn_discover_bottom)
        val btnCart : LinearLayout = findViewById(R.id.btn_cart_bottom)
        var btnProfile : LinearLayout = findViewById(R.id.btn_profile_bottom)
//        btnHome.setOnClickListener {
//
//            startActivity(Intent(applicationContext,MainActivity::class.java))
//
//        }
        btnDiscover.setOnClickListener {
            startActivity(Intent(applicationContext,ProductActivity::class.java))

        }
        btnCart.setOnClickListener {
            startActivity(Intent(applicationContext,CartActivity::class.java))
        }
        btnProfile.setOnClickListener {
            startActivity(Intent(applicationContext,ProfileActivity::class.java))
        }
    }

    private fun recyclerViewVoucherAction() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewVoucher.layoutManager = linearLayoutManager
        arrVoucher = ArrayList()
        arrVoucher.add("Free Ship 200%")
        arrVoucher.add("-50% Price")
        arrVoucher.add("Freedom in Hell")
        arrVoucher.add("-200% of Quantity Price")

        adapterVoucher = VoucherAdapter(arrVoucher)
        recyclerViewVoucher.adapter = adapterVoucher

    }

    private fun recyclerViewBestSeller() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSeller.layoutManager = linearLayoutManager

        var productList: ArrayList<Product> = ArrayList()
        adapterBestSeller = BestSellerAdapter(this,productList)
        recyclerViewSeller.adapter = adapterBestSeller

        var requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
        var jsonArrayRequest = JsonArrayRequest(Server.linkSeller, { response ->
            var id = 0
            var name = ""
            var thumb = ""
            var description = ""
            var price = 0.0
            var calories = 0
            var sale = 0
            var unit = ""
            if (response != null) {

                for (i in 0 until response.length()) {
                    var jsonObject: JSONObject? = response.getJSONObject(i)
                    id = jsonObject!!.getInt("id")
                    name = jsonObject!!.getString("name")
                    thumb = jsonObject!!.getString("thumb")
                    description = jsonObject!!.getString("description")
                    price = jsonObject!!.getDouble("price")
                    calories = jsonObject!!.getInt("calories")
                    sale = jsonObject!!.getInt("sale")
                    unit = jsonObject!!.getString("unit")

                    productList.add(Product(id, name, thumb,description,price, calories, sale, unit))
                    adapterBestSeller.notifyDataSetChanged()
                }
            }
        }, { error ->
            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            Log.d("DDDD", error.toString())
        })
        requestQueue.add(jsonArrayRequest)

//        adapterBestSeller = BestSellerAdapter(this,productList)
        recyclerViewSeller.adapter = adapterBestSeller

    }

    private fun recyclerViewCategory() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleViewCate.layoutManager = linearLayoutManager

        var cateList: ArrayList<Category> = ArrayList()
        adapterCate = CategoryAdapter(this,cateList)
        recycleViewCate.adapter = adapterCate

        var requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
        var jsonArrayRequest = JsonArrayRequest(Server.linkCategory, { response ->
            var id = 0
            var name = ""
            var thumb = ""
            if (response != null) {

                for (i in 0 until response.length()) {
                    var jsonObject: JSONObject? = response.getJSONObject(i)
                    id = jsonObject!!.getInt("id")
                    name = jsonObject!!.getString("name")
                    thumb = jsonObject!!.getString("thumb")

                    cateList.add(Category(id, name, thumb))
                    adapterCate.notifyDataSetChanged()
                }
            }
        }, { error ->
            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            Log.d("DDDD", error.toString())
        })
        requestQueue.add(jsonArrayRequest)

    }

    private fun actionViewFlipper() {
        //get array
        arrBanner = ArrayList()
        var requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
        var jsonArrayRequest = JsonArrayRequest(Server.linkBanner, { response ->

            if (response != null) {
                for (i in 0 until response.length()) {
                    val link = response.getString(i)
                    arrBanner.add(link)


                    adapterCate.notifyDataSetChanged()
                }

                // add image into ViewFlipper
                for (i in 0 until arrBanner.size) {
                    var imgView = ImageView(applicationContext)
                    Glide.with(applicationContext).load(arrBanner.get(i)).into(imgView)
                    imgView.scaleType = ImageView.ScaleType.FIT_XY
                    viewFlipper.addView(imgView)

                }
                viewFlipper.flipInterval = 5000
                viewFlipper.isAutoStart = true
                // viewFlipper.alpha = 0.8f
                val slide_in: Animation = AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_in_right
                )
                val slide_out: Animation = AnimationUtils.loadAnimation(
                    applicationContext,
                    R.anim.slide_out_right
                )
                viewFlipper.inAnimation = slide_in
                viewFlipper.outAnimation = slide_out

            }
        }, { error ->
            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            Log.d("DDDD", error.toString())
        })

        requestQueue.add(jsonArrayRequest)
    }


    private fun initView() {
        viewFlipper = findViewById(R.id.view_pager)
        recycleViewCate = findViewById(R.id.view_cate)
        recyclerViewSeller = findViewById(R.id.view_seller)
        recyclerViewVoucher = findViewById(R.id.view_voucher)
        imgAvt = findViewById(R.id.img_avt)
    }

    private fun getDataUser() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val link: String = Server.linkUser
        val stringRequest =
            object : StringRequest(Method.POST, link, { response ->
                var name = ""
                var avt = ""
                if (response != null && response.length > 3) {
                    Log.d("getCart", "user : $response")
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        name = jsonObject.getString("name")
                        avt = jsonObject.getString("avt")

                        Glide.with(this).load(avt).into(imgAvt)
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
}