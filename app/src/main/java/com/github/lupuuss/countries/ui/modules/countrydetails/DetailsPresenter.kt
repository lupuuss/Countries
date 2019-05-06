package com.github.lupuuss.countries.ui.modules.countrydetails

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import timber.log.Timber
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
     private val countriesManager: CountriesManager
) : BasePresenter<DetailsView>(), BiConsumer<List<RawCountryDetails>, Throwable> {

    var subscription: Disposable? = null

    fun onDisplayCountryDetailsRequest(countryName: String) {

        subscription?.dispose()

        subscription = countriesManager.getCountryDetails(countryName)
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