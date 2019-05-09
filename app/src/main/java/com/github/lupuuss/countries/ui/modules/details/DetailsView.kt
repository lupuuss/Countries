package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.DynamicContentView
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.google.android.gms.maps.model.LatLng

interface DetailsView : DynamicContentView {

    fun displayFlag(flagLink: String)
    fun displayCountryDetails(countryDetails: RawCountryDetails)
    fun centerMap(latLng: LatLng, zoom: Float)
    var isNoLocationErrorVisible: Boolean
}