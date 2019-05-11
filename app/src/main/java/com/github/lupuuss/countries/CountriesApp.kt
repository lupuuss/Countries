package com.github.lupuuss.countries

import com.github.lupuuss.countries.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.HasActivityInjector
import timber.log.Timber

class CountriesApp : DaggerApplication(), HasActivityInjector {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()
    }

    companion object {
        const val API_URL = "https://restcountries.eu/rest/v2/"
    }
}