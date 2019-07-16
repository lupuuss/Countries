package com.github.lupuuss.countries.model.countries

import com.github.lupuuss.countries.model.dataclass.BoundingBox
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import io.reactivex.Single

interface CountriesManager {

    class NoDetailsException : Throwable()

    interface CountriesListChangedListener {
        fun onCountriesListChanged(countries: List<ShortCountry>)
        fun onCountriesListRequestFail(exception: Throwable)
    }

    fun provideList()
    fun refreshList()
    fun getCountryDetails(countryName: String): Single<RawCountryDetails>
    fun addOnCountriesListChangedListener(onCountriesListChangedListener: CountriesListChangedListener)
    fun removeOnCountriesListChangedListener(onCountriesListChangedListener: CountriesListChangedListener)
    fun getBoundingBox(code: String, onBoundingBoxReady: (BoundingBox?) -> Unit)
}