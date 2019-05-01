package com.github.lupuuss.countries.ui.modules.main

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.BasicCountryInfo
import timber.log.Timber
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val countriesManager: CountriesManager
) : BasePresenter<MainView>(), CountriesManager.CountriesListChangedListener {

    override fun onCountriesListChanged(countries: List<BasicCountryInfo>) {
        Timber.d(countries.toString())
    }

    override fun onCountriesListRequestFail(exception: Throwable) {
        Timber.d(exception)
    }

    override fun attachView(view: MainView) {
        super.attachView(view)

        countriesManager.provideList()
        countriesManager.addOnCountriesListChangedListener(this)
    }

    override fun detachView() {

        countriesManager.removeOnCountriesListChangedListener(this)
        super.detachView()
    }
}