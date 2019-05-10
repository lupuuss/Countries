package com.github.lupuuss.countries.di

import android.content.Context
import com.github.lupuuss.countries.model.environment.AndroidEnvironmentInteractor
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.google.android.gms.common.GoogleApiAvailability
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class TestAndroidModule(private val context: Context) {

    @Provides
    @TestAppComponentScope
    fun providesContext() = context

    @Provides
    @TestAppComponentScope
    fun providesSchedulersPackage() =
        SchedulersPackage(Schedulers.trampoline(), Schedulers.trampoline())

    @Provides
    @TestAppComponentScope
    fun providesEnvironment(): EnvironmentInteractor = mock { }

    @Provides
    @TestAppComponentScope
    fun providesGoogleApiAvailability(): GoogleApiAvailability = mock { }
}