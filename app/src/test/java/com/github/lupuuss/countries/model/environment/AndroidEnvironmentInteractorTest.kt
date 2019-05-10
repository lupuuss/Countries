package com.github.lupuuss.countries.model.environment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

import org.junit.Assert.*

private fun contextWithPassedNetworkStatus(network: Boolean): Context {

    return mock {
        on { getSystemService(Context.CONNECTIVITY_SERVICE) }.then {
            mock <ConnectivityManager> {
                on { activeNetworkInfo }.then {
                    mock <NetworkInfo>{
                        on {  isConnected }.then { network }
                    }
                }
            }
        }
    }
}

class AndroidEnvironmentInteractorTestNetworkAndMapNotAvailable {

    // simulates unavailable network
    private val context: Context = contextWithPassedNetworkStatus(false)
    private val googleApiAvailability: GoogleApiAvailability = mock {
        on { isGooglePlayServicesAvailable(any()) }.then { ConnectionResult.API_UNAVAILABLE }
        on { isUserResolvableError(any()) }.then { false }
    }
    private val environment = AndroidEnvironmentInteractor(
        context, googleApiAvailability
    )

    @Test
    fun isNetworkAvailable_shouldReturnFalse_whenNetworkNotAvailable() {

        assertEquals(false, environment.isNetworkAvailable())
    }

    @Test
    fun isMapAvailable_shouldReturnMapNotAvailable_whenGoogleServicesNotAvailable() {

        val status = environment.isMapAvailable()

        assertEquals(MapStatus.Code.UNAVAILABLE, status.statusCode)
        assert(!status.isAvailable()){ "MapStatus.isAvailable should be false!" }
        assert(status.statusMessage?.contains("Google Services code: ") ?: true) {
            "Status message should contains Google Services code"
        }
    }
}

class AndroidEnvironmentInteractorTestNetworkAndMapAvailable {

    // simulates unavailable network
    private val context: Context = contextWithPassedNetworkStatus(true)
    private val googleApiAvailability: GoogleApiAvailability = mock {
        on { isGooglePlayServicesAvailable(any()) }.then { ConnectionResult.SUCCESS }
        on { isUserResolvableError(any()) }.then { false }
    }
    private val environment = AndroidEnvironmentInteractor(
        context, googleApiAvailability
    )

    @Test
    fun isNetworkAvailable_shouldReturnTrue_whenNetworkAvailable() {

        assertEquals(true, environment.isNetworkAvailable())
    }

    @Test
    fun isMapAvailable_shouldReturnMapAvailable_whenGoogleServicesAvailable() {

        val status = environment.isMapAvailable()

        assertEquals(MapStatus.Code.AVAILABLE, status.statusCode)
        assert(status.isAvailable()){ "MapStatus.isAvailable should be true!" }
        assert(status.statusMessage?.contains("Google Services code: ") ?: true) {
            "Status message should contains Google Services code"
        }
    }
}

class AndroidEnvironmentInteractorTestMapNeedsUsersActions {

    // simulates unavailable network
    private val context: Context = contextWithPassedNetworkStatus(true)
    private val googleApiAvailability: GoogleApiAvailability = mock {
        on { isGooglePlayServicesAvailable(any()) }.then { ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED}
        on { isUserResolvableError(any()) }.then { true }
    }
    private val environment = AndroidEnvironmentInteractor(
        context, googleApiAvailability
    )

    @Test
    fun isMapAvailable_shouldReturnMapNeedsUsersActions_whenGoogleServicesNeedsActions() {

        val status = environment.isMapAvailable()

        assertEquals(MapStatus.Code.NEEDS_USER_ACTIONS, status.statusCode)
        assert(!status.isAvailable()){ "MapStatus.isAvailable should be false!" }
        assert(status.statusMessage?.contains("Google Services code: ") ?: true) {
            "Status message should contains Google Services code"
        }
    }
}


