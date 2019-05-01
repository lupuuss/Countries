package com.github.lupuuss.countries.ui.modules.main

import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.model.dataclass.ErrorMessage

interface MainView : BaseView {
    fun postString(msg: String)
    fun displayCountriesList(countries: List<ShortCountry>)
    fun showErrorMsg(errorMsg: ErrorMessage)
}