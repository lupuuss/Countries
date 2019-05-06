package com.github.lupuuss.countries.ui.modules.countrydetails

import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails

interface DetailsView : BaseView {

    fun displayFlag(flagLink: String)
    fun displayCountryDetails(countryDetails: RawCountryDetails)
}