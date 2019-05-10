package com.github.lupuuss.countries

import android.app.Activity
import android.app.Application
import com.github.lupuuss.countries.di.TestAndroidModule
import com.github.lupuuss.countries.di.DaggerTestAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TestCountriesApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()

        DaggerTestAppComponent
            .builder()
            .testAndroidModule(TestAndroidModule(this.applicationContext))
            .build()
            .inject(this)
    }
}