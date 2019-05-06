package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.DynamicContentView
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails

interface DetailsView : DynamicContentView {

    fun displayFlag(flagLink: String)
    fun displayCountryDetails(countryDetails: RawCountryDetails)
}