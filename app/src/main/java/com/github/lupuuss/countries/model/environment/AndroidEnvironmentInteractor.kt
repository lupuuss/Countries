package com.github.lupuuss.countries.model.environment

import android.content.Context
import android.net.ConnectivityManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class AndroidEnvironmentInteractor(
    private val context: Context,
    private val googleApiAvailability: GoogleApiAvailability
): EnvironmentInteractor {

    class GoogleApiStatus(
        override val statusCode: MapStatus.Code,
        val googleApiStatusCode: Int
        ) : MapStatus {

        override val statusMessage: String?
            get() = "Google Services code: $googleApiStatusCode"
    }

    override fun isNetworkAvailable(): Boolean {

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = manager!!.activeNetworkInfo
        var isAvailable = false

        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }
        return isAvailable
    }


    override fun isMapAvailable(): MapStatus {

        val googleStatus = googleApiAvailability.isGooglePlayServicesAvailable(context)

        return when {
            googleStatus == ConnectionResult.SUCCESS -> GoogleApiStatus(MapStatus.Code.AVAILABLE, googleStatus)
            googleApiAvailability.isUserResolvableError(googleStatus) -> GoogleApiStatus(MapStatus.Code.NEEDS_USER_ACTIONS, googleStatus)
            else -> GoogleApiStatus(MapStatus.Code.UNAVAILABLE, googleStatus)
        }
    }
}