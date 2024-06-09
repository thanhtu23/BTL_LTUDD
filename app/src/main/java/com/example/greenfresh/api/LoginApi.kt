package com.example.greenfresh.api

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class LoginApi {

    fun saveIdUser(context: Context, uid: Int) {
        var sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "user",
            AppCompatActivity.MODE_PRIVATE
        )
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("uid", uid)
        editor.apply()
    }
    fun getIdUser(context: Context): Int{
        var sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "user",
            AppCompatActivity.MODE_PRIVATE
        )
        var uid = sharedPreferences.getInt("uid",0)
        return uid
    }
    fun clearIdUser(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "user",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("uid")
        editor.apply()
    }

}