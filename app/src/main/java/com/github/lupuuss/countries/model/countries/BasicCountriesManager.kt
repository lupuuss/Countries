package com.github.lupuuss.countries.model.countries

import android.annotation.SuppressLint
import com.github.lupuuss.countries.di.SchedulersPackage
import com.github.lupuuss.countries.model.dataclass.ShortCountry

class BasicCountriesManager(
    private val countriesApi: CountriesApi,
    private val schedulersPackage: SchedulersPackage
) : CountriesManager {

    private var countryList: List<ShortCountry>? = null
    private var lastError: Throwable? = null
    private val listeners: MutableList<CountriesManager.CountriesListChangedListener> = mutableListOf()

    /**
     * Ensures that countries list has been requested.
     */
    override fun provideList() {

        if (countryList == null && lastError == null) {
            requestCountriesList()
        }
    }

    @SuppressLint("CheckResult")
    private fun requestCountriesList() {

        countriesApi.getCountries()
            .observeOn(schedulersPackage.frontScheduler)
            .subscribeOn(schedulersPackage.backScheduler)
            .subscribe { countries, throwable ->

                countryList = countries
                countries?.let {
                    listeners.forEach { listener -> listener.onCountriesListChanged(it) }
                }

                lastError = throwable
                throwable?.let {
                    listeners.forEach { listener -> listener.onCountriesListRequestFail(it) }
                }
            }
    }

    /**
     * Force countries list request.
     */
    override fun refreshList() {

        requestCountriesList()
    }

    override fun addOnCountriesListChangedListener(onCountriesListChangedListener: CountriesManager.CountriesListChangedListener) {
        listeners.add(onCountriesListChangedListener)

        countryList?.let {
            onCountriesListChangedListener.onCountriesListChanged(it)
        }

        lastError?.let {
            onCountriesListChangedListener.onCountriesListRequestFail(it)
        }
    }

    override fun removeOnCountriesListChangedListener(onCountriesListChangedListener: CountriesManager.CountriesListChangedListener) {
        listeners.remove(onCountriesListChangedListener)
    }

}