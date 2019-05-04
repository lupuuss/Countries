package com.github.lupuuss.countries.model.countries

import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import com.github.lupuuss.countries.model.dataclass.ShortCountry
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {

    @GET("all?fields=name;flag")
    fun getCountries(): Single<List<ShortCountry>>

    @GET("name/{name}")
    fun getCountryDetails(@Path("name") countryName: String): Single<List<RawCountryDetails>>
}