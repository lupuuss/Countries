package com.github.lupuuss.countries.ui.modules.main

import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test


class MainPresenterTestInternetAvailable {

    private val environment: EnvironmentInteractor = mock { on { isNetworkAvailable() }.then { true } }
    private val countriesManager: CountriesManager = mock { }
    private val presenter: MainPresenter = MainPresenter(countriesManager, environment)
    private val view: MainView = mock { }

    @Before
    fun setUp() {

        presenter.attachView(view)
    }

    @Test
    fun attachView_shouldProvideCountriesListAndRegisterListener() {

        verify(countriesManager, times(1)).provideList()
        verify(countriesManager, times(1)).addOnCountriesListChangedListener(presenter)
    }

    @Test
    fun detachView_shouldUnregisterListeners() {

        presenter.detachView()

        verify(countriesManager, times(1)).removeOnCountriesListChangedListener(presenter)
    }

    @Test
    fun onCountriesListChanged_shouldDisplayListOnView() {
        val list: List<ShortCountry> = listOf()

        presenter.onCountriesListChanged(list)

        verify(view, times(1)).displayCountriesList(list)
        verify(view, times(1)).isProgressBarVisible = false
        verify(view, times(1)).isErrorMessageVisible = false
    }

    @Test
    fun onCountriesListRequestFail_shouldShowUnknownErrorAndPrintMsg_whenNetworkAvailable() {
        val throwable = Throwable("Error")

        presenter.onCountriesListRequestFail(throwable)

        verify(view, times(1)).showErrorMsg(ErrorMessage.UNKNOWN)
        verify(view, times(1)).postString(eq("Error"))
        verify(view, times(1)).isProgressBarVisible = false
        verify(view, times(1)).isErrorMessageVisible = true
    }

    @Test
    fun refreshCountriesList() {

        presenter.refreshCountriesList()

        verify(view, times(1)).isProgressBarVisible = true
        verify(view, times(1)).isErrorMessageVisible = false
        verify(countriesManager, times(1)).refreshList()
    }

    @Test
    fun filterCountriesList() {

        presenter.filterCountriesList("sample")

        verify(view, times(1)).filterCountriesList(eq("sample"))
    }
}

class MainPresenterTestInternetNotAvailable {

    private val environment: EnvironmentInteractor = mock { on { isNetworkAvailable() }.then { false } }
    private val countriesManager: CountriesManager = mock { }
    private val presenter: MainPresenter = MainPresenter(countriesManager, environment)
    private val view: MainView = mock { }

    @Before
    fun setUp() {

        presenter.attachView(view)
    }

    @Test
    fun onCountriesListRequestFail_shouldShowErrorNoInternetConnection_whenNetworkNotAvailable() {
        val throwable = Throwable("Error")

        presenter.onCountriesListRequestFail(throwable)

        verify(view, times(1)).showErrorMsg(ErrorMessage.NO_INTERNET_CONNECTION)
        verify(view, never()).postString(any())
    }
}