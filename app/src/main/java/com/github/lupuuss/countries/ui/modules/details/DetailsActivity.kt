package com.github.lupuuss.countries.ui.modules.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
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
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.base.DynamicContentActivity
import com.github.lupuuss.countries.kotlin.SafeVar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class DetailsActivity : DynamicContentActivity(), OnMapReadyCallback, DetailsView, View.OnClickListener {

    /**
     * SafeVar avoids uninitialized variable error when map loads to slow.
     */
    private val map = SafeVar<GoogleMap>()
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

    override var isNoLocationErrorVisible = false
        get () = noLocationErrorMessageView?.isVisible ?: field
        set(value) {
            field = value
            noLocationErrorMessageView?.isVisible = value
        }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.refreshButtonView -> presenter.resendDetailsRequest()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(detailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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

    override fun onMapReady(googleMap: GoogleMap) {

        map.value = googleMap
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun centerMap(latLng: LatLng, zoom: Float) {

        map.use {

            it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        }
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

            if (value != null && value != "") {

                view.append(" $value")
            } else {

                view.append(" -")
            }

            detailsContainer.addView(view)
        }
    }

    private fun createRegionString(countryDetails: RawCountryDetails): String {

        val subregion = countryDetails.subregion
        val region = countryDetails.region

        return if (region == null) {
            "-"
        } else if (subregion != null && subregion.contains(region)){

            subregion

        } else if (subregion != null) {

            "$region, $subregion"

        } else {
            region
        }
    }

    private fun concatCodes(countryDetails: RawCountryDetails): String {
        var concat = countryDetails.numericCode ?: " - "
        concat += countryDetails.alpha2Code ?: "-"
        concat += "/"
        concat += countryDetails.alpha3Code ?: "-"
        return concat
    }


    /**
     * This function defines country details order
     */
    private fun RawCountryDetails.toCountryDetailsMap() = mapOf(
            "capital" to capital,
            "region" to createRegionString(this),
            "regional_blocks" to flatList(regionalBlocs, RawCountryDetails.RegionalBloc::name),
            "area" to  formatAny(area, " km2"),
            "population" to formatAny(population),
            "gini" to formatAny(gini, "%"),
            "demonym" to demonym,
            "currency" to currencies.firstOrNull()?.toCurrencyString(),
            "domain" to topLevelDomain.firstOrNull(),
            "languages" to flatList(languages, RawCountryDetails.Language::name),
            "codes" to concatCodes(this),
            "calling_code" to flatList(callingCodes) { "+$it" },
            "timezones" to flatList(timezones) { it },
            "borders" to flatList(borders) { it }
    )

    override fun displayFlag(flagLink: String?) {

        if (flagLink != null) {
            svgLoader.with(this).load(flagLink, flagImage)
        } else {

            imageContainer.isGone = true
        }
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
