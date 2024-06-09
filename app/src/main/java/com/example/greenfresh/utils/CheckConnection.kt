package com.example.greenfresh.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class CheckConnection {
    companion object {
        fun isConnected(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifi: NetworkInfo? =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile: NetworkInfo? =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            return (wifi != null && wifi.isConnected) || (mobile != null && mobile.isConnected)
        }
    }
}