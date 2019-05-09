package com.github.lupuuss.countries.di

import android.content.Context
import com.github.lupuuss.countries.model.environment.AndroidEnvironmentInteractor
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class AndroidModule(private val context: Context) {

    @Provides
    @AppComponentScope
    fun providesContext() = context

    @Provides
    @AppComponentScope
    fun providesSchedulersPackage() = SchedulersPackage(Schedulers.io(), AndroidSchedulers.mainThread())

    @Provides
    @AppComponentScope
    fun providesEnvironment(context: Context, googleApiAvailability: GoogleApiAvailability): EnvironmentInteractor =
        AndroidEnvironmentInteractor(context, googleApiAvailability)

    @Provides
    @AppComponentScope
    fun providesGoogleApiAvailability(): GoogleApiAvailability = GoogleApiAvailability.getInstance()
}