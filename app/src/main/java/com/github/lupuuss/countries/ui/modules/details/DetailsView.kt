package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.DynamicContentView
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.environment.MapStatus
import com.google.android.gms.maps.model.LatLng

interface DetailsView : DynamicContentView {

    var isNoLocationErrorVisible: Boolean
    fun displayCountryDetails(countryDetails: RawCountryDetails)
    fun displayFlag(flagLink: String?)
    fun showMapsNeedsActionsDialog(mapStatus: MapStatus)
    fun centerMap(latLng: LatLng, zoom: Float)
    fun centerMap(sw: LatLng, ne: LatLng)
}