package com.github.lupuuss.countries.model.countries

import com.github.lupuuss.countries.model.dataclass.BasicCountryInfo
import io.reactivex.Single
import retrofit2.http.GET

interface CountriesApi {

    @GET("all?fields=name;flag")
    fun getCountries(): Single<List<BasicCountryInfo>>
}