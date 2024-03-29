package com.github.lupuuss.countries.model.environment

import android.content.Context
import android.net.ConnectivityManager
import com.github.lupuuss.countries.R
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

    override fun getCountriesBoxesJson(): String =
        context.getString(R.string.countries_boxes_json)

    override fun isNetworkAvailable(): Boolean {

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = manager!!.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnected
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