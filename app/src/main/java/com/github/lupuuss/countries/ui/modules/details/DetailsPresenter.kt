package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.calculateZoom
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.github.lupuuss.countries.model.environment.MapStatus
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import timber.log.Timber
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
     private val countriesManager: CountriesManager,
     private val gson: Gson,
     private val environment: EnvironmentInteractor
) : BasePresenter<DetailsView>(), BiConsumer<RawCountryDetails, Throwable> {

    private var lastRequest: String? = null
    private var subscription: Disposable? = null
    var state: String? = null

    fun provideCountryDetails(countryName: String) {

        // if state is saved don't call API

        lastRequest = countryName

        if (state != null) {

            // simulate successful response

            this.accept(gson.fromJson(state, RawCountryDetails::class.java), null)
            return
        }

        sendRequest(countryName)
    }

    private fun sendRequest(countryName: String) {

        lastRequest = countryName

        subscription?.dispose()

        subscription = countriesManager.getCountryDetails(countryName)
            .map {
                state = gson.toJson(it)
                it
            }
            .subscribe(this)
    }

    override fun accept(result: RawCountryDetails?, error: Throwable?) {

        error?.let {

            displayError(error)
            return
        }

        result?.let { countryDetails ->

            val mapStatus = environment.isMapAvailable()

            // checks if map is available and location for country is provided
            // if it's not show errors
            centerOnCountryIfPossible(mapStatus, countryDetails)


            view?.displayCountryDetails(countryDetails)
            view?.displayFlag(countryDetails.flag)

            view?.isProgressBarVisible = false
            view?.isErrorMessageVisible = false
            view?.isContentVisible = true
        }
    }

    private fun centerOnCountryIfPossible(mapStatus: MapStatus, countryDetails: RawCountryDetails) {

        Timber.d(mapStatus.statusMessage)

        if (mapStatus.statusCode == MapStatus.Code.AVAILABLE && countryDetails.latlng.size >= 2) {

            val latlng = countryDetails.latlng
            view?.isNoLocationErrorVisible = false
            view?.centerMap(LatLng(latlng[0], latlng[1]), calculateZoom(countryDetails))
            return

        }

        if (countryDetails.latlng.size < 2) {

            view?.isNoLocationErrorVisible = true

        }

        if (mapStatus.statusCode == MapStatus.Code.NEEDS_USER_ACTIONS) {

            view?.showMapsNeedsActionsDialog(mapStatus)

        } else if (mapStatus.statusCode == MapStatus.Code.UNAVAILABLE){

            view?.postMessage(BaseView.Message.GOOGLE_MAPS_UNAVAILABLE)
        }
    }

    private fun displayError(error: Throwable) {

        Timber.e(error)

        when {

            environment.isNetworkAvailable() -> {

                view?.setErrorMsg(ErrorMessage.UNKNOWN)
                view?.postString(error.localizedMessage)

            }
            error is CountriesManager.NoDetailsException -> view?.setErrorMsg(ErrorMessage.COUNTRY_NOT_FOUND)
            else -> view?.setErrorMsg(ErrorMessage.NO_INTERNET_CONNECTION)
        }

        view?.isProgressBarVisible = false
        view?.isErrorMessageVisible = true
        view?.isNoLocationErrorVisible = false
        view?.isContentVisible = false
    }

    override fun detachView() {
        super.detachView()
        subscription?.dispose()
    }

    fun resendDetailsRequest() {

        lastRequest?.let {

            view?.isErrorMessageVisible = false
            view?.isProgressBarVisible = true
            view?.isContentVisible = false
            view?.isNoLocationErrorVisible = false
            sendRequest(it)
        }
    }
}