package com.github.lupuuss.countries.ui.modules.countrydetails

import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.model.dataclass.CountryDetails

interface DetailsView : BaseView {


    fun displayCountryDetails(countryDetails: CountryDetails)
    fun displayFlag(flagLink: String)
}