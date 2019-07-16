package com.github.lupuuss.countries.model.countries

import android.annotation.SuppressLint
import com.github.lupuuss.countries.di.SchedulersPackage
import com.github.lupuuss.countries.kotlin.SafeVar
import com.github.lupuuss.countries.model.dataclass.BoundingBox
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single

@SuppressLint("CheckResult")
class BasicCountriesManager(
    private val countriesApi: CountriesApi,
    private val schedulersPackage: SchedulersPackage,
    private val environmentInteractor: EnvironmentInteractor,
    private val gson: Gson
) : CountriesManager {

    private var countryList: List<ShortCountry>? = null
    private var lastError: Throwable? = null
    private val listeners: MutableList<CountriesManager.CountriesListChangedListener> = mutableListOf()
    private val boundingBoxes: SafeVar<Map<String, BoundingBox>> = SafeVar()

    init {

        // reads bounding boxes from json (it's provided by Android resources)
        Single.fromCallable {

            gson.fromJson<Map<String, BoundingBox>>(
                environmentInteractor.getCountriesBoxesJson(),
                object : TypeToken<Map<String, BoundingBox>>() {}.type
            )
        }
            .observeOn(schedulersPackage.frontScheduler)
            .subscribeOn(schedulersPackage.backScheduler)
            .subscribe { boxes ->
                boundingBoxes.value = boxes
            }
    }

    /**
     * Ensures that countries list has been requested.
     */
    override fun provideList() {

        if (countryList == null && lastError == null) {
            requestCountriesList()
        }
    }

    @SuppressLint("CheckResult")
    private fun requestCountriesList() {

        countriesApi.getCountries()
            .observeOn(schedulersPackage.frontScheduler)
            .subscribeOn(schedulersPackage.backScheduler)
            .subscribe { countries, throwable ->

                countryList = countries
                countries?.let {
                    listeners.forEach { listener -> listener.onCountriesListChanged(it) }
                }

                lastError = throwable
                throwable?.let {
                    listeners.forEach { listener -> listener.onCountriesListRequestFail(it) }
                }
            }
    }

    /**
     * Force countries list request.
     */
    override fun refreshList() {

        requestCountriesList()
    }

    /**
     * Changes country codes in borders field to names.
     */
    private fun transformBorder(details: RawCountryDetails): RawCountryDetails {

        val borders = details.borders

        countryList?.let { list ->

            val newBorders = borders.map { border ->
                val info = list.find {
                    it.alpha3Code == border
                }

                info?.name ?: border
            }

            return RawCountryDetails(details, newBorders)
        }

        return details
    }

    override fun getCountryDetails(countryName: String): Single<RawCountryDetails> {
        return countriesApi
            .getCountryDetails(countryName)
            .flatMap { result ->

                if (result.isEmpty()) {

                    Single.error(CountriesManager.NoDetailsException())

                } else {

                    Single.just(transformBorder(result.first()))

                }
            }
            .observeOn(schedulersPackage.frontScheduler)
            .subscribeOn(schedulersPackage.backScheduler)
    }

    override fun getBoundingBox(code: String, onBoundingBoxReady: (BoundingBox?) -> Unit) {

        boundingBoxes.use {

            onBoundingBoxReady(it[code])
        }
    }

    override fun addOnCountriesListChangedListener(onCountriesListChangedListener: CountriesManager.CountriesListChangedListener) {
        listeners.add(onCountriesListChangedListener)

        countryList?.let {
            onCountriesListChangedListener.onCountriesListChanged(it)
        }

        lastError?.let {
            onCountriesListChangedListener.onCountriesListRequestFail(it)
        }
    }

    override fun removeOnCountriesListChangedListener(onCountriesListChangedListener: CountriesManager.CountriesListChangedListener) {
        listeners.remove(onCountriesListChangedListener)
    }

}