package com.github.lupuuss.countries.model.environment

import android.content.Context
import android.net.ConnectivityManager

class AndroidEnvironmentInteractor(private val context: Context): EnvironmentInteractor {

    override fun isNetworkAvailable(): Boolean {

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = manager!!.activeNetworkInfo
        var isAvailable = false

        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }
        return isAvailable
    }
}