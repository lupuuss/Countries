package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.calculateZoom
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
     private val countriesManager: CountriesManager,
     private val gson: Gson,
     private val environment: EnvironmentInteractor
) : BasePresenter<DetailsView>(), BiConsumer<List<RawCountryDetails>, Throwable> {

    private var lastRequest: String? = null
    private var subscription: Disposable? = null
    var state: String? = null

    private fun sendRequest(countryName: String) {

        lastRequest = countryName

        subscription?.dispose()

        subscription = countriesManager.getCountryDetails(countryName)
            .map {
                state = gson.toJson(it.first())
                it
            }
            .subscribe(this)
    }

    fun provideCountryDetails(countryName: String) {

        // if state is saved don't call API

        lastRequest = countryName

        if (state != null) {

            // simulate successful response

            this.accept(listOf(gson.fromJson(state, RawCountryDetails::class.java)), null)
            return
        }

        sendRequest(countryName)
    }

    override fun accept(result: List<RawCountryDetails>?, error: Throwable?) {


        result?.let {

            val countryDetails = it.firstOrNull()

            when {
                countryDetails == null -> {

                    view?.showErrorMsg(ErrorMessage.COUNTRY_NOT_FOUND)
                    return
                }

                countryDetails.latlng.isEmpty() -> view?.isNoLocationErrorVisible = true

                else -> {

                    val latlng = countryDetails.latlng
                    view?.isNoLocationErrorVisible = false
                    view?.centerMap(LatLng(latlng[0], latlng[1]), calculateZoom(countryDetails))

                }
            }

            view?.displayCountryDetails(countryDetails)
            view?.displayFlag(countryDetails.flag)

            view?.isProgressBarVisible = false
            view?.isErrorMessageVisible = false
            view?.isContentVisible = true
        }

        error?.let {

            if (environment.isNetworkAvailable()) {

                view?.showErrorMsg(ErrorMessage.UNKNOWN)

            } else {

                view?.showErrorMsg(ErrorMessage.NO_INTERNET_CONNECTION)
            }

            view?.isProgressBarVisible = false
            view?.isErrorMessageVisible = true
            view?.isNoLocationErrorVisible = false
            view?.isContentVisible = false
        }
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