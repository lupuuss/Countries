package com.github.lupuuss.countries.model.countries

import com.github.lupuuss.countries.di.SchedulersPackage
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.nhaarman.mockitokotlin2.*
import io.reactivex.internal.operators.single.SingleError
import io.reactivex.internal.operators.single.SingleJust
import io.reactivex.schedulers.Schedulers
import org.junit.Assert

import org.junit.Test

class BasicCountriesManagerTestRequestOk {

    private val sampleList = listOf(ShortCountry("Poland","POL"))
    private val sampleDetails: RawCountryDetails = RawCountryDetails(
        "", "", "", 0.0, "", "",
        "", "",0.0, "",
        "", 1, "", "", mock {},
        listOf(), listOf("POL"), listOf(), mock {}, mock {},
        listOf(0.0, 0.0), mock {}, listOf(), listOf()
    )

    private val countriesApi: CountriesApi = mock {
        on { getCountries() }.then { SingleJust(sampleList) }
        on { getCountryDetails(eq("Poland")) }.then { SingleJust(listOf(sampleDetails)) }
        on { getCountryDetails(eq("empty")) }.then {
            SingleJust(listOf<RawCountryDetails>()) }
    }
    private val countriesManager: BasicCountriesManager =
        BasicCountriesManager(
            countriesApi,
            SchedulersPackage(Schedulers.trampoline(), Schedulers.trampoline())
        )

    private val mockedListener: CountriesManager.CountriesListChangedListener = mock {}

    @Test
    fun provideList_shouldCallApi_whenFirstUse() {

        countriesManager.provideList()
        verify(countriesApi, times(1)).getCountries()
        countriesManager.provideList()
        verify(countriesApi, times(1)).getCountries()
    }

    @Test
    fun refreshList_shouldCallCountriesAPI() {

        countriesManager.refreshList()
        verify(countriesApi, times(1)).getCountries()
    }

    @Test
    fun addOnCountriesListChangedListener_shouldCallOnListChanged_whenRequestOk() {

        countriesManager.addOnCountriesListChangedListener(mockedListener)
        countriesManager.refreshList()

        verify(mockedListener, times(1)).onCountriesListChanged(eq(sampleList))
        verify(mockedListener, never()).onCountriesListRequestFail(any())
    }

    @Test
    fun addOnCountriesListChangedListener_shouldCallOnListChanged_whenRequestDoneBeforeRegister() {

        countriesManager.provideList()
        countriesManager.addOnCountriesListChangedListener(mockedListener)

        verify(mockedListener, times(1)).onCountriesListChanged(sampleList)
        verify(mockedListener, never()).onCountriesListRequestFail(any())
    }

    @Test
    fun removeOnCountriesListChangedListener_shouldAvoidOnListChangedCall() {

        countriesManager.addOnCountriesListChangedListener(mockedListener)
        countriesManager.removeOnCountriesListChangedListener(mockedListener)
        countriesManager.refreshList()

        verify(mockedListener, never()).onCountriesListChanged(any())
        verify(mockedListener, never()).onCountriesListRequestFail(any())
    }

    @Test
    fun refreshList_shouldAlwaysCallAPI() {

        countriesManager.refreshList()
        verify(countriesApi, times(1)).getCountries()

        countriesManager.refreshList()
        verify(countriesApi, times(2)).getCountries()

    }

    @Test
    fun getCountryDetails_shouldReturnRxWithTransformedBorders() {

        countriesManager.provideList()
        var value: RawCountryDetails? = null
        countriesManager.getCountryDetails("Poland").subscribe { v ->
            value = v
        }
        Assert.assertEquals("Poland", value!!.borders.first() )
    }

    @Test
    fun getCountryDetails_shouldReturnSubscriptionWithError_whenEmptyResult() {

        var error: Throwable? = null
        var result: RawCountryDetails? = null

        countriesManager.getCountryDetails("empty").subscribe { res, err ->
            error = err
            result = res
        }

        Assert.assertEquals(CountriesManager.NoDetailsException::class, error!!::class)
        Assert.assertEquals(null, result)
    }
}

class BasicCountriesManagerTestRequestFail {

    private val sampleError =  Throwable("Error")

    private val countriesApi: CountriesApi = mock {
        on { getCountries() }.then { SingleError<List<ShortCountry>>{sampleError} }
    }
    private val countriesManager: BasicCountriesManager =
        BasicCountriesManager(
            countriesApi,
            SchedulersPackage(Schedulers.trampoline(), Schedulers.trampoline())
        )

    private val mockedListener: CountriesManager.CountriesListChangedListener = mock {}

    @Test
    fun addOnCountriesListChangedListener_shouldCallOnListChanged_whenRequestDoneBeforeRegister() {

        countriesManager.provideList()
        countriesManager.addOnCountriesListChangedListener(mockedListener)

        verify(mockedListener, never()).onCountriesListChanged(any())
        verify(mockedListener, times(1)).onCountriesListRequestFail(sampleError)
    }

    @Test
    fun addOnCountriesListChangedListener_shouldCallOnListChanged_whenRequestFail() {

        countriesManager.addOnCountriesListChangedListener(mockedListener)
        countriesManager.refreshList()

        verify(mockedListener, never()).onCountriesListChanged(any())
        verify(mockedListener, times(1)).onCountriesListRequestFail(eq(sampleError))
    }
}