package com.example.greenfresh.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.greenfresh.R
import com.example.greenfresh.activity.login.LoginActivity


class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        Handler(Looper.getMainLooper()).postDelayed({
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        },2000)
    }
}