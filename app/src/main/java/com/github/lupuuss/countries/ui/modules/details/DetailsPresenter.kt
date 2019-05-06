package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import timber.log.Timber
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
     private val countriesManager: CountriesManager,
     private val gson: Gson
) : BasePresenter<DetailsView>(), BiConsumer<List<RawCountryDetails>, Throwable> {

    private var subscription: Disposable? = null
    var state: String? = null

    fun onDisplayCountryDetailsRequest(countryName: String) {

        // if state is saved don't call API

        if (state != null) {

            // simulate successful response

            this.accept(listOf(gson.fromJson(state, RawCountryDetails::class.java)), null)
            return
        }

        subscription?.dispose()

        subscription = countriesManager.getCountryDetails(countryName)
            .map {
                state = gson.toJson(it.first())
                it
            }
            .subscribe(this)
    }

    override fun accept(result: List<RawCountryDetails>?, error: Throwable?) {

        result?.let {
            val countryDetails = it.first()
            view?.displayCountryDetails(countryDetails)
            view?.displayFlag(countryDetails.flag)
        }

        error?.let { Timber.d(it) }
    }

    override fun detachView() {
        super.detachView()
        subscription?.dispose()
    }
}