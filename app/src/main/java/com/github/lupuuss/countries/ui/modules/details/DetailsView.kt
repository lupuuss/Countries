package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.DynamicContentView
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.github.lupuuss.countries.model.environment.MapStatus
import com.google.android.gms.maps.model.LatLng

interface DetailsView : DynamicContentView {

    fun displayCountryDetails(countryDetails: RawCountryDetails)
    fun centerMap(latLng: LatLng, zoom: Float)
    var isNoLocationErrorVisible: Boolean
    fun displayFlag(flagLink: String?)
    fun showMapsNeedsActionsDialog(mapStatus: MapStatus)
}