package com.github.lupuuss.countries.ui.modules.details

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.ahmadrosid.svgloader.SvgLoader
import com.github.lupuuss.countries.TestCountriesApp
import com.github.lupuuss.countries.R
import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.kotlin.AntiSpam
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.environment.AndroidEnvironmentInteractor
import com.github.lupuuss.countries.model.environment.MapStatus
import com.google.android.gms.common.ConnectionResult
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestCountriesApp::class)
class DetailsActivityTest {

    private fun sampleRawDetials(
        nativeName: String? = "Polska",
        region: String? = "Europ",
        subregion: String? = "Eastern Europ"
    ) =
        RawCountryDetails(
            "Poland", "PL", "POL", 312679.0, "Warsaw", "POL", "Polish", "flaglink.com",
            34.1, nativeName, "616", 38437239, region, subregion, mock {},
            listOf(), listOf("Ukraine", "Germany", "Slovakia"), listOf("48"),
            listOf(RawCountryDetails.Currency("", "Polish złoty", "zł")),
            listOf(RawCountryDetails.Language("", "", "Polish", "")),
            listOf(0.0, 0.0), listOf(RawCountryDetails.RegionalBloc("", "European Union", listOf(), listOf())),
            listOf(), listOf(".pl")

        )

    private lateinit var activityController: ActivityController<DetailsActivity>

    private lateinit var activity: DetailsActivity

    @Before
    fun setUp(){

        val intent = Intent()

        intent.putExtra(DetailsActivity.COUNTRY_NAME, "Poland")
        activityController = Robolectric.buildActivity(DetailsActivity::class.java, intent)
        activity = activityController.setup().get()
    }

    @Test
    fun isNoLocationErrorVisible_shouldReturnNoLocationViewVisibility() {

        val view = activity.findViewById<View>(R.id.noLocationErrorMessageView)

        view.isVisible = false
        Assert.assertEquals(false, activity.isNoLocationErrorVisible)

        view.isVisible = true

        Assert.assertEquals(true, activity.isNoLocationErrorVisible)
    }

    @Test
    fun isNoLocationErrorVisible_shouldSetNoLocationViewVisibility() {

        val view = activity.findViewById<View>(R.id.noLocationErrorMessageView)

        activity.isNoLocationErrorVisible = false

        Assert.assertEquals(false, view.isVisible)

        activity.isNoLocationErrorVisible = true

        Assert.assertEquals(true, view.isVisible)
    }

    @Test
    fun onCreate_shouldSetOnClickListenerOnRefreshButton() {

        val view = Shadows.shadowOf(activity.findViewById<Button>(R.id.refreshButtonView))

        Assert.assertEquals(activity, view.onClickListener)
    }

    @Test
    fun onCreate_shouldSetCountryLabel() {

        val countryText: TextView = activity.findViewById(R.id.countryNameTextView)

        Assert.assertEquals("Poland", countryText.text)
    }

    @Test
    fun onCreate_shouldAttachToPresenter() {

        verify(activity.presenter, times(1)).attachView(activity)
    }

    @Test
    fun onCreate_shouldRestorePresenterState() {

        verify(activity.presenter, times(1)).state = anyOrNull()
    }

    @Test
    fun onCreate_shouldCallProvideDetailsOnPresenter() {

        verify(activity.presenter, times(1)).provideCountryDetails("Poland")
    }

    @Test
    fun onClick_shouldCallPresenterThroughAntiSpam_whenIdRefreshButtonView() {

        activity.onClick(mock { on { id }.then { R.id.refreshButtonView }})
        activity.onClick(mock { on { id }.then { 0 }})

        verify(activity.antiSpam, times(1))
            .doAction(eq("DetailsActivity.onClickRefreshButton"), eq(AntiSpam.STANDARD_DELTA), any())

        verify(activity.presenter, times(1)).resendDetailsRequest()
    }

    @Test
    fun onOptionsItemSelected_shouldCallOnBackPressed_whenIdHome() {

        val spied = spy(activity)

        spied.onOptionsItemSelected(mock { on { itemId }.then { android.R.id.home } })
        spied.onOptionsItemSelected(mock { on { itemId }.then { 0 } })

        verify(spied, times(1)).onBackPressed()
    }

    @Test
    fun onBackPressed_shouldFinishActivity() {

        activity.onBackPressed()

        Assert.assertEquals(true, activity.isFinishing)
    }

    @Test
    fun onSaveInstanceState_shouldSavePresenterState() {

        activity.presenter = mock { on { state }.then { "Any state" }}

        val bundle = Bundle()
        activityController.saveInstanceState(bundle)

        Assert.assertEquals("Any state", bundle.getString(DetailsActivity.SAVED_COUNTRY_DATA))
    }

    @Test
    fun onDestroy_shouldDetachView() {

        activityController.destroy()

        verify(activity.presenter, times(1)).detachView()
    }

    @Test
    fun displayCountryDetails_shouldSetOnlyRegion_whenSubregionNull() {

        activity.displayCountryDetails(sampleRawDetials(region = "Asia", subregion = null))

        val region = activity.findViewById<LinearLayout>(R.id.detailsContainer).getChildAt(2) as TextView

        Assert.assertEquals("Region: Asia", region.text.toString())
    }

    @Test
    fun displayCountryDetails_shouldSetRegionAsSubregion_whenSubregionContainsRegion() {

        activity.displayCountryDetails(sampleRawDetials(region = "Asia", subregion = "Western Asia"))

        val region = activity.findViewById<LinearLayout>(R.id.detailsContainer).getChildAt(2) as TextView

        Assert.assertEquals("Region: Western Asia", region.text.toString())
    }

    @Test
    fun displayCountryDetails_shouldSetRegion_whenRegionIsNull() {

        activity.displayCountryDetails(sampleRawDetials(region = null))

        val region = activity.findViewById<LinearLayout>(R.id.detailsContainer).getChildAt(2) as TextView

        Assert.assertEquals("Region: -", region.text.toString())
    }

    @Test
    fun displayCountryDetails_shouldFillLayoutInProperOrder_whenDataNotNull() {

        activity.displayCountryDetails(sampleRawDetials())

        val properlyFormattedTexts = listOf(
            "Capital: Warsaw",
            "Region: Eastern Europ",
            "Regional blocks: European Union",
            "Area: 312,679 km²",
            "Population: 38,437,239",
            "Gini ratio: 34.1%",
            "Demonym: Polish",
            "Currency: Polish złoty (zł)",
            "Domain: .pl",
            "Languages: Polish",
            "Country codes: 616 PL/POL",
            "Calling codes: +48",
            "Timezones: -",
            "Borders: Ukraine, Germany, Slovakia"
            )

        val container = activity.findViewById<LinearLayout>(R.id.detailsContainer)

        for (index in 1 until container.childCount) {


            Assert.assertEquals(
                properlyFormattedTexts[index - 1],
                (container.getChildAt(index) as TextView).text.toString()
            )
        }
    }

    @Test
    fun displayCountryDetails_shouldDisplayOnlyName_whenNativeNameEmpty() {

        activity.displayCountryDetails(sampleRawDetials(nativeName = ""))

        val view = activity.findViewById<TextView>(R.id.countryNameTextView)

        Assert.assertEquals("Poland", view.text)
    }

    @Test
    fun displayCountryDetails_shouldDisplayOnlyName_whenNativeNameSameAsName() {

        activity.displayCountryDetails(sampleRawDetials(nativeName = "Poland"))

        val view = activity.findViewById<TextView>(R.id.countryNameTextView)

        Assert.assertEquals("Poland", view.text)
    }

    @Test
    fun displayCountryDetails_shouldDisplayOnlyName_whenNativeNameNull() {

        activity.displayCountryDetails(sampleRawDetials(nativeName = null))

        val view = activity.findViewById<TextView>(R.id.countryNameTextView)

        Assert.assertEquals("Poland", view.text)
    }

    @Test
    fun displayCountryDetails_shouldDisplayNativeNameInLabel_whenNativeNameNotNull() {

        activity.displayCountryDetails(sampleRawDetials())

        val view = activity.findViewById<TextView>(R.id.countryNameTextView)

        Assert.assertEquals("Poland (Polska)", view.text)
    }

    @Test
    fun displayFlag_shouldHideImageContainer_whenLinkNull() {

        activity.displayFlag(null)

        val view = activity.findViewById<FrameLayout>(R.id.imageContainer)

        Assert.assertEquals(true, view.isGone)
    }

    @Test
    fun displayFlag_shouldUseSvgLoader_whenLinkNotNull() {

        val chainSvgMock = mock <SvgLoader> { }
        val specialSvgLoaderMock = mock <SvgLoader> { on { with(any()) }.then { chainSvgMock }}
        val flagImage = activity.findViewById<ImageView>(R.id.flagImage)

        activity.svgLoader = specialSvgLoaderMock
        activity.displayFlag("anylink.com")


        verify(chainSvgMock, times(1)).load(eq("anylink.com"), eq(flagImage))
        verify(specialSvgLoaderMock, times(1)).with(eq(activity))
    }

    @Test
    fun  showMapsNeedsActionsDialog_shouldDelegateToGoogleApiClass() {

        val status = AndroidEnvironmentInteractor.GoogleApiStatus(
            MapStatus.Code.NEEDS_USER_ACTIONS,
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
        )

        activity.showMapsNeedsActionsDialog(status)

        verify(activity.googleApiAvailability, times(1)).showErrorDialogFragment(
                activity, status.googleApiStatusCode, DetailsActivity.GOOGLE_SERVICES_REQUEST
        )
    }

    @Test
    fun  showMapsNeedsActionsDialog_shouldPostMessage_whenStatusNotFromGoogle() {

        val status = object : MapStatus {
            override val statusMessage: String? = "Any"
            override val statusCode: MapStatus.Code = MapStatus.Code.AVAILABLE
        }

        val spied  = spy(activity)

        spied.showMapsNeedsActionsDialog(status)

        verify(spied, times(1)).postMessage(BaseView.Message.GOOGLE_MAPS_UNAVAILABLE)
    }

}