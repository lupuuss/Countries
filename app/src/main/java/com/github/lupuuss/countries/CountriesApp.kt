package com.github.lupuuss.countries

import android.app.Activity
import android.app.Application
import com.github.lupuuss.countries.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class CountriesApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingAndroidInjector


    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()
        DaggerAppComponent.create().inject(this)
    }

    companion object {
        const val API_URL = "https://restcountries.eu/rest/v2/"
    }
}