package com.github.lupuuss.countries.ui.modules.main

import android.view.View
import com.github.lupuuss.countries.base.DynamicContentView
import com.github.lupuuss.countries.model.dataclass.ShortCountry

interface MainView : DynamicContentView {
    fun displayCountriesList(countries: List<ShortCountry>)
    fun onClick(p0: View?)
    fun filterCountriesList(query: String)
    fun clearCountriesList()
    fun navigateToCountryDetails(name: String)
}