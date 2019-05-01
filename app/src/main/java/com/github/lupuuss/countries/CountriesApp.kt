package com.github.lupuuss.countries

import android.app.Application

class CountriesApp : Application() {


    companion object {
        const val API_URL = "https://restcountries.eu/rest/v2/"
    }
}