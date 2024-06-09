package com.example.greenfresh.activity


import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.greenfresh.R
import com.example.greenfresh.api.LoginApi
import com.example.greenfresh.model.Product
import com.example.greenfresh.api.CartApi


class DetailProductActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var title : TextView
    lateinit var pic : ImageView
    lateinit var tvStar : TextView
    lateinit var ratingStar : RatingBar
    lateinit var tvCalo : TextView
    lateinit var tvPrice : TextView
    lateinit var tvPrice_discount: TextView
    lateinit var tv_description : TextView
    lateinit var tv_unit : TextView
    lateinit var plus : TextView
    lateinit var minus : TextView
    lateinit var numberCart : TextView
    lateinit var total_price : TextView
    lateinit var btnAddCart : ConstraintLayout
    var number : Int = 1

    var userId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        userId = LoginApi().getIdUser(this)
        Log.d("CCC", "onCreate: $userId")
        actionToolbar()
        initView()

        setView()
    }

    private fun setView() {
        var product : Product = intent.getSerializableExtra("product") as Product

        title.text = product.name
        tvCalo.text =product.calories.toString()+" Kcal"
        tvPrice.text = "$"+product.price.toString()
        tvPrice_discount.text = "$"+(product.price-product.price*product.sale/100.0).toString()
        if(product.sale ==0){
            tvPrice_discount.visibility = View.INVISIBLE
            tvPrice.setTextColor(resources.getColor(R.color.black))
            tvPrice.paintFlags = 0
        }

        Glide.with(applicationContext).load(product.thumb).into(pic)

        tv_description.text = product.description
        tv_unit.text = "Unit: "+product.unit

        plus.setOnClickListener {
            number++

            numberCart.text = number.toString()

        }
        minus.setOnClickListener {
            number--
            if(number<1){
                number=1
            }
            numberCart.text = number.toString()
        }
        btnAddCart.setOnClickListener {
            CartApi().addToCart(this, userId,product.id,number)
        }
    }

    private fun initView() {
        pic = findViewById(R.id.img_detail)
        tvStar = findViewById(R.id.tv_star)
        ratingStar = findViewById(R.id.rating_star)
        tvCalo = findViewById(R.id.tv_calo)
        tvPrice = findViewById(R.id.tvPrice)
        tvPrice_discount = findViewById(R.id.tv_price_real)
        tv_description = findViewById(R.id.tv_description)
        tv_unit = findViewById(R.id.tv_unit)
        plus = findViewById(R.id.plus)
        minus = findViewById(R.id.minus)
        total_price = findViewById(R.id.tv_total_price)
        btnAddCart = findViewById(R.id.btn_add_to_cart)
        numberCart = findViewById(R.id.tv_number)
        title = findViewById(R.id.toolbar_title)
    }

    private fun actionToolbar() {
        toolbar= findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


}