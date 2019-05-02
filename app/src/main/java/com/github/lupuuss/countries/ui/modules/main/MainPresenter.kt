package com.github.lupuuss.countries.ui.modules.main

import com.github.lupuuss.countries.base.BasePresenter
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.model.dataclass.ErrorMessage
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val countriesManager: CountriesManager,
    private val environment: EnvironmentInteractor
) : BasePresenter<MainView>(), CountriesManager.CountriesListChangedListener {

    override fun attachView(view: MainView) {
        super.attachView(view)

        countriesManager.provideList()
        countriesManager.addOnCountriesListChangedListener(this)
    }

    override fun detachView() {

        countriesManager.removeOnCountriesListChangedListener(this)
        super.detachView()
    }

    override fun onCountriesListChanged(countries: List<ShortCountry>) {

        view?.isErrorMessageVisible = false
        view?.isProgressBarVisible = false
        view?.displayCountriesList(countries)
    }

    override fun onCountriesListRequestFail(exception: Throwable) {

        if (environment.isNetworkAvailable()) {

            view?.showErrorMsg(ErrorMessage.UNKNOWN)
            view?.postString(exception.localizedMessage)

        } else {

            view?.showErrorMsg(ErrorMessage.NO_INTERNET_CONNECTION)
        }
        view?.isErrorMessageVisible = true
        view?.isProgressBarVisible = false
    }

    fun onClickRefreshButton() {

        view?.clearCountriesList()
        view?.isErrorMessageVisible = false
        view?.isProgressBarVisible = true
        countriesManager.refreshList()
    }

    fun onQueryTextChanged(newText: String?) {

        view?.filterCountriesList(newText ?: "")
    }

    fun onCountryClick(name: String) {
        view?.navigateToCountryDetails(name)
    }
}