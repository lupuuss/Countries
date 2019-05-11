package com.github.lupuuss.countries.ui.modules.details

import com.github.lupuuss.countries.base.BaseView
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.environment.AndroidEnvironmentInteractor
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.github.lupuuss.countries.model.environment.MapStatus
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.*
import io.reactivex.internal.operators.single.SingleJust
import org.junit.Assert
import org.junit.Test

import org.junit.Before

class DetailsPresenterTestRequestMapAvailable {

    private val sampleDetailsAllOK = RawCountryDetails(
        "", "", "", 0.0, "", "",
        "", "anyflaglink.com",0.0, "",
        "", 1, "", "", mock {},
        listOf(), listOf("POL"), listOf(), mock {}, mock {},
        listOf(0.0, 0.0), mock {}, listOf(), listOf()
    )

    private val sampleDetailsNoFlag = RawCountryDetails(
        "", "", "", 0.0, "", "",
        "", null,0.0, "",
        "", 1, "", "", mock {},
        listOf(), listOf("POL"), listOf(), mock {}, mock {},
        listOf(0.0, 0.0), mock {}, listOf(), listOf()
    )

    private val sampleDetailsNoLocation = RawCountryDetails(
        "", "", "", 0.0, "", "",
        "", null,0.0, "",
        "", 1, "", "", mock {},
        listOf(), listOf("POL"), listOf(), mock {}, mock {},
        listOf(), mock {}, listOf(), listOf()
    )

    private val countriesManager: CountriesManager = mock {
        on { getCountryDetails(any()) }.then { SingleJust<RawCountryDetails>(sampleDetailsAllOK) }
    }

    private val gson: Gson = mock {
        on { toJson(sampleDetailsAllOK) }.then { "Just json" }
        on { fromJson(any<String>(), any<Class<*>>()) }.then { sampleDetailsAllOK }
    }

    private val view: DetailsView = mock { }

    private val environment: EnvironmentInteractor = mock {
        on { isMapAvailable() }.then {
            AndroidEnvironmentInteractor.GoogleApiStatus(MapStatus.Code.AVAILABLE, 0)
        }
    }

    @Before
    fun setUp() {

        presenter.attachView(view)
    }

    private val presenter = DetailsPresenter(countriesManager, gson, environment)
    @Test
    fun getState_shouldReturnNull_beforeApiResponse() {

        Assert.assertEquals(null , presenter.state)
    }

    @Test
    fun getState_shouldReturnState_afterApiResponse() {

        presenter.provideCountryDetails("any")

        Assert.assertEquals("Just json", presenter.state)
    }

    @Test
    fun setState() {
        presenter.state = "State"

        Assert.assertEquals("State", presenter.state)
    }

    @Test
    fun provideCountryDetails_shouldCallApi_whenStateNull() {

        presenter.state = null

        presenter.provideCountryDetails("any")

        verify(countriesManager, times(1)).getCountryDetails(eq("any"))
        verify(view, times(1)).displayCountryDetails(sampleDetailsAllOK)
    }

    @Test
    fun provideCountryDetails_shouldUseSavedState_whenStateNotNull() {

        presenter.state = "Any state"

        presenter.provideCountryDetails("any")

        verify(countriesManager, times(0)).getCountryDetails(any())
        verify(view, times(1)).displayCountryDetails(eq(sampleDetailsAllOK))
    }

    @Test
    fun accept_shouldSetDetails_whenRequestOk() {

        presenter.accept(sampleDetailsAllOK, null)

        verify(view, times(1)).displayCountryDetails(sampleDetailsAllOK)
        verify(view, times(1)).isProgressBarVisible = false
        verify(view, times(1)).isContentVisible = true
        verify(view, times(1)).isErrorMessageVisible = false
    }

    @Test
    fun accept_shouldDisplayFlag_whenFlagAvailable() {

        presenter.accept(sampleDetailsAllOK, null)

        verify(view, times(1)).displayFlag(eq("anyflaglink.com"))
    }

    @Test
    fun accept_shouldCallDisplayFlagWithNull_whenFlagNotAvailable() {

        presenter.accept(sampleDetailsNoFlag, null)

        verify(view, times(1)).displayFlag(eq(null))
    }

    @Test
    fun accept_shouldCenterMap_whenMapAvailable() {

        presenter.accept(sampleDetailsAllOK, null)

        verify(view, times(1)).centerMap(eq(LatLng(0.0, 0.0)), any())
    }

    @Test
    fun accept_shouldShowLocationError_whenNoLocation() {

        presenter.accept(sampleDetailsNoLocation, null)

        verify(view, times(1)).isNoLocationErrorVisible = true
    }

    @Test
    fun resendDetailsRequest_shouldDoNothing_whenLastRequestNull() {

        presenter.attachView(view)

        presenter.resendDetailsRequest()

        verifyZeroInteractions(view)
        verifyZeroInteractions(gson)
        verifyZeroInteractions(environment)
        verifyZeroInteractions(countriesManager)
    }

    @Test
    fun resendDetailsRequest_shouldAlwaysCallApiAndPrepareView_whenLastRequestNotNull() {

        presenter.attachView(view)
        presenter.provideCountryDetails("any")

        presenter.resendDetailsRequest()

        verify(view, atLeast(1)).isContentVisible = false
        verify(view, atLeast(1)).isNoLocationErrorVisible = false
        verify(view, atLeast(1)).isErrorMessageVisible = false
        verify(view, atLeast(1)).isProgressBarVisible = true
    }

}

class DetailsPresenterTestRequestMapNotAvailable {

    private val sampleDetailsAllOK = RawCountryDetails(
        "", "", "", 0.0, "", "",
        "", "anyflaglink.com",0.0, "",
        "", 1, "", "", mock {},
        listOf(), listOf("POL"), listOf(), mock {}, mock {},
        listOf(0.0, 0.0), mock {}, listOf(), listOf()
    )

    private val countriesManager: CountriesManager = mock {
        on { getCountryDetails(any()) }.then { SingleJust<RawCountryDetails>(sampleDetailsAllOK) }
    }

    private val gson: Gson = mock {
        on { toJson(sampleDetailsAllOK) }.then { "Just json" }
        on { fromJson(any<String>(), any<Class<*>>()) }.then { sampleDetailsAllOK }
    }

    private val view: DetailsView = mock { }

    private val environment: EnvironmentInteractor = mock {
        on { isMapAvailable() }.then {
            AndroidEnvironmentInteractor.GoogleApiStatus(MapStatus.Code.UNAVAILABLE, ConnectionResult.API_UNAVAILABLE)
        }
    }

    private val presenter = DetailsPresenter(countriesManager, gson, environment)

    @Before
    fun setUp() {

        presenter.attachView(view)
    }

    @Test
    fun accept_shouldPostMapsError_whenMapsNoAvailable() {

        presenter.accept(sampleDetailsAllOK, null)

        verify(view, times(1)).postMessage(BaseView.Message.GOOGLE_MAPS_UNAVAILABLE)
    }

}

class DetailsPresenterTestRequestMapsNeedsUserActions {

    private val sampleDetailsAllOK = RawCountryDetails(
        "", "", "", 0.0, "", "",
        "", "anyflaglink.com",0.0, "",
        "", 1, "", "", mock {},
        listOf(), listOf("POL"), listOf(), mock {}, mock {},
        listOf(0.0, 0.0), mock {}, listOf(), listOf()
    )

    private val countriesManager: CountriesManager = mock {
        on { getCountryDetails(any()) }.then { SingleJust<RawCountryDetails>(sampleDetailsAllOK) }
    }

    private val gson: Gson = mock {
        on { toJson(sampleDetailsAllOK) }.then { "Just json" }
        on { fromJson(any<String>(), any<Class<*>>()) }.then { sampleDetailsAllOK }
    }

    private val view: DetailsView = mock { }

    private val mapsNeedsUserActionsStatus = AndroidEnvironmentInteractor.GoogleApiStatus(
        MapStatus.Code.NEEDS_USER_ACTIONS,
        ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
    )

    private val environment: EnvironmentInteractor = mock {
        on { isMapAvailable() }.then { mapsNeedsUserActionsStatus }
    }

    private val presenter = DetailsPresenter(countriesManager, gson, environment)

    @Before
    fun setUp() {

        presenter.attachView(view)
    }

    @Test
    fun accept_shouldPostMapsError_whenMapsNoAvailable() {

        presenter.accept(sampleDetailsAllOK, null)

        verify(view, times(1)).showMapsNeedsActionsDialog(mapsNeedsUserActionsStatus)
    }

}