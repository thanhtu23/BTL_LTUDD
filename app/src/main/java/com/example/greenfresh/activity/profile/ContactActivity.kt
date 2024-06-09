package com.example.greenfresh.activity.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.greenfresh.R

class ContactActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var btn_phone: LinearLayout
    lateinit var btn_facebook: LinearLayout
    lateinit var btn_website: LinearLayout
    lateinit var btn_address: LinearLayout
    lateinit var btn_twitter: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        initView()
        actionToolBar()

        btn_phone.setOnClickListener {
            var intent : Intent = Intent()
            intent.setAction(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:0123456789"))
            startActivity(intent)
        }
        btn_facebook.setOnClickListener {
            var intent : Intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.facebook.com/"))
            startActivity(intent)
        }
        btn_website.setOnClickListener {
            var intent : Intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.google.com/"))
            startActivity(intent)
        }
        btn_twitter.setOnClickListener {
            var intent : Intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.setData(Uri.parse("https://www.google.com/"))
            startActivity(intent)
        }
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_contact)
        btn_phone = findViewById(R.id.btn_phone)
        btn_facebook = findViewById(R.id.btn_facebook)
        btn_website = findViewById(R.id.btn_website)
        btn_address = findViewById(R.id.btn_address)
        btn_twitter = findViewById(R.id.btn_twitter)
    }

    private fun actionToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}