package com.github.lupuuss.countries

import android.app.Activity
import android.app.Application
import com.nhaarman.mockitokotlin2.mock
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector


class TestCountriesApp : Application(), HasActivityInjector {

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? = mock{}

}