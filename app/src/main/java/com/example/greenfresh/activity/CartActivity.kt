package com.example.greenfresh.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.greenfresh.R
import com.example.greenfresh.activity.profile.ProfileActivity
import com.example.greenfresh.fragment.CartFragment
import com.example.greenfresh.fragment.EmptyCartFragment
import com.example.greenfresh.api.CartApi
import com.example.greenfresh.api.LoginApi

class CartActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var title: TextView
    lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initView()
        bottomNavagation()
        fragmentManager = supportFragmentManager
        CartApi().isEmptyCart(this) {
            if (it) {
                getFragmentEmptyCart()
            } else {
                getFragmentCart()
            }

        }
    }

    private fun bottomNavagation() {
        val drawable = resources.getDrawable(R.drawable.ic_baseline_shopping_cart, null)
        // Set the color filter on the drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.setColorFilter(resources.getColor(R.color.green, null), PorterDuff.Mode.SRC_IN)
        }
        // Set the drawable to an ImageView or other view
        //        imageView.setImageDrawable(drawable)

        val img: ImageView = findViewById(R.id.imgViewCart)
        val text: TextView = findViewById(R.id.tvCart)
        img.setImageDrawable(drawable)
        text.setTextColor(ContextCompat.getColor(applicationContext, R.color.green))

        var btnHome: LinearLayout = findViewById(R.id.btn_home_bottom)
        var btnDiscover: LinearLayout = findViewById(R.id.btn_discover_bottom)
        var btnCart: LinearLayout = findViewById(R.id.btn_cart_bottom)
        var btnProfile: LinearLayout = findViewById(R.id.btn_profile_bottom)
        btnHome.setOnClickListener {

            startActivity(Intent(applicationContext, MainActivity::class.java))

        }
        btnDiscover.setOnClickListener {
            startActivity(Intent(applicationContext, ProductActivity::class.java))

        }
//        btnCart.setOnClickListener {
//            startActivity(Intent(applicationContext, CartActivity::class.java))
//        }
        btnProfile.setOnClickListener {
            startActivity(Intent(applicationContext, ProfileActivity::class.java))
        }
    }

    private fun getFragmentCart() {
        val ft: FragmentTransaction = fragmentManager.beginTransaction()

        ft.replace(R.id.frameContent, CartFragment())
        ft.commit()
    }

    private fun getFragmentEmptyCart() {

        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.add(R.id.frameContent, EmptyCartFragment(), "fragEmp")
        ft.commit()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbarCart)
        title = findViewById(R.id.mycart)


    }
}