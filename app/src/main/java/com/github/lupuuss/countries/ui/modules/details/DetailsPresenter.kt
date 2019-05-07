package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.calculateZoom
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
     private val countriesManager: CountriesManager,
     private val gson: Gson
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

    fun onDisplayCountryDetailsRequest(countryName: String) {

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

            view?.isProgressBarVisible = false
            view?.isErrorMessageVisible = false
            view?.isContentVisible = true

            val countryDetails = it.first()

            view?.displayCountryDetails(countryDetails)
            view?.displayFlag(countryDetails.flag)
            val latlng = countryDetails.latlng
            view?.centerMap(LatLng(latlng[0], latlng[1]), calculateZoom(countryDetails))
        }

        error?.let {

            view?.isProgressBarVisible = false
            view?.isErrorMessageVisible = true
            view?.isContentVisible = false
            view?.showErrorMsg(ErrorMessage.UNKNOWN)
        }
    }

    override fun detachView() {
        super.detachView()
        subscription?.dispose()
    }

    fun onClickRefreshButton() {

        lastRequest?.let {

            view?.isErrorMessageVisible = false
            view?.isProgressBarVisible = true
            view?.isContentVisible = false
            sendRequest(it)
        }
    }
}