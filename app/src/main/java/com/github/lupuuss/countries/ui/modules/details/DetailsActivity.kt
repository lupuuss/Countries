package com.github.lupuuss.countries.ui.modules.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.ahmadrosid.svgloader.SvgLoader
import com.github.lupuuss.countries.flatList
import com.github.lupuuss.countries.formatAny
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject
import androidx.core.text.toSpanned
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.base.DynamicContentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class DetailsActivity : DynamicContentActivity(), OnMapReadyCallback, DetailsView, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var countryName: String

    @Inject
    lateinit var presenter: DetailsPresenter

    @Inject
    lateinit var svgLoader: SvgLoader

    override val errorTextView: TextView?
        get() = errorMessageTextView

    override val progressBar: ProgressBar?
        get() = detailsProgressBar

    override val content: View?
        get() = detailsContainer

    override val refreshButton: Button?
        get() = refreshButtonView

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.refreshButtonView -> presenter.resendDetailsRequest()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        countryName = intent!!.extras!!.getString(COUNTRY_NAME)!!
        countryNameTextView.text = countryName

        refreshButton?.setOnClickListener(this)

        presenter.attachView(this)

        presenter.state = savedInstanceState?.getString(SAVED_COUNTRY_DATA)

        presenter.provideCountryDetails(countryName)
    }

    override fun centerMap(latLng: LatLng, zoom: Float) {

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    @SuppressLint("SetTextI18n")
    override fun displayCountryDetails(countryDetails: RawCountryDetails) {

        val countryDetailsMap = countryDetails.toCountryDetailsMap()

        if (countryName != countryDetails.nativeName && countryDetails.nativeName != "") {

            countryNameTextView.text = "$countryName (${countryDetails.nativeName})"
        }

        // details order is defined by toCountryDetailsMap()
        countryDetailsMap.forEach { (name, value) ->

            val id = this@DetailsActivity.resources.getIdentifier(
                name,
                "string",
                this@DetailsActivity.packageName
            )

            val view = this@DetailsActivity
                .layoutInflater
                .inflate(R.layout.country_detail_text, detailsContainer, false)

            val text = getText(id).toSpanned()

            (view as TextView).text = text

            if (value != "") {

                view.append(" $value")
            } else {

                view.append(" -")
            }

            detailsContainer.addView(view)
        }
    }

    private fun createRegionString(countryDetails: RawCountryDetails): String {

        return if (countryDetails.region == "") {
            "-"
        } else if (countryDetails.subregion != "" && countryDetails.subregion.contains(countryDetails.region)){

            return countryDetails.subregion

        } else if (countryDetails.subregion != "") {

            return "${countryDetails.region}, ${countryDetails.subregion}"

        } else {

            return countryDetails.region
        }
    }

    private fun concatCodes(countryDetails: RawCountryDetails): String =
        "${countryDetails.numericCode}, ${countryDetails.alpha2Code}, ${countryDetails.alpha3Code} "

    /**
     * This function defines country details order
     */
    private fun RawCountryDetails.toCountryDetailsMap() = mapOf(
            "capital" to capital,
            "region" to createRegionString(this),
            "regionalBlocks" to flatList(regionalBlocs, RawCountryDetails.RegionalBloc::name),
            "area" to (if (area != 0.0) "${formatAny(area)} km2" else " - "),
            "population" to formatAny(population),
            "gini" to "${formatAny(gini)}%",
            "demonym" to demonym,
            "currency" to (currencies.firstOrNull()?.toCurrencyString() ?: ""),
            "domain" to (topLevelDomain.firstOrNull() ?: ""),
            "languages" to flatList(languages, RawCountryDetails.Language::name),
            "codes" to concatCodes(this),
            "callingCode" to flatList(callingCodes) { "+$it" },
            "timezones" to flatList(timezones) { it },
            "borders" to flatList(borders) { it }
    )

    override fun displayFlag(flagLink: String) {

        svgLoader.with(this).load(flagLink, flagImage)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putString(SAVED_COUNTRY_DATA, presenter.state)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {

        const val COUNTRY_NAME = "countryName"
        private const val SAVED_COUNTRY_DATA = "savedCountryData"
    }
}
