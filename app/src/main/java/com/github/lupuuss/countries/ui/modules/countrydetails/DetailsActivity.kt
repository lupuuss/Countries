package com.github.lupuuss.countries.ui.modules.countrydetails

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ahmadrosid.svgloader.SvgLoader
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.databinding.ActivityDetailsBinding
import com.github.lupuuss.countries.model.dataclass.CountryDetails
import com.google.android.gms.maps.*
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback, DetailsView {

    private lateinit var mMap: GoogleMap
    private lateinit var countryName: String
    private lateinit var binding: ActivityDetailsBinding

    @Inject
    lateinit var presenter: DetailsPresenter

    @Inject
    lateinit var svgLoader: SvgLoader


    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        countryName = intent!!.extras!!.getString(COUNTRY_NAME)!!
        countryNameTextView.text = countryName
        presenter.attachView(this)
        presenter.onDisplayCountryDetailsRequest(countryName)
    }

    override fun displayCountryDetails(countryDetails: CountryDetails) {
        binding.details = countryDetails
        binding.executePendingBindings()
    }

    override fun displayFlag(flagLink: String) {

        svgLoader.with(this).load(flagLink, flagImage)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {
        const val COUNTRY_NAME = "countryName"
    }
}
