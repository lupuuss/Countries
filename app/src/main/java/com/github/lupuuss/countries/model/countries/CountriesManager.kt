package com.github.lupuuss.countries.model.countries

import com.github.lupuuss.countries.model.dataclass.BasicCountryInfo

interface CountriesManager {

    interface CountriesListChangedListener {
        fun onCountriesListChanged(countries: List<BasicCountryInfo>)
        fun onCountriesListRequestFail(exception: Throwable)
    }

    fun provideList()
    fun refreshList()
    fun addOnCountriesListChangedListener(onCountriesListChangedListener: CountriesListChangedListener)
    fun removeOnCountriesListChangedListener(onCountriesListChangedListener: CountriesListChangedListener)
}