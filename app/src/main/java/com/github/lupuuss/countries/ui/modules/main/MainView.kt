package com.github.lupuuss.countries.ui.modules.main

import android.view.View
import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.model.dataclass.ErrorMessage

interface MainView : BaseView {
    var isProgressBarVisible: Boolean
    var isErrorMessageVisible: Boolean
    fun postString(msg: String)
    fun displayCountriesList(countries: List<ShortCountry>)
    fun showErrorMsg(errorMsg: ErrorMessage)
    fun onClick(p0: View?)
    fun filterCountriesList(query: String)
    fun clearCountriesList()
    fun navigateToCountryDetails(name: String)
}