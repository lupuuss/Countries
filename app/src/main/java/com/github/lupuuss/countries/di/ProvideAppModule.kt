package com.github.lupuuss.countries.di

import android.content.Context
import com.github.lupuuss.countries.kotlin.AntiSpam
import com.github.lupuuss.countries.model.environment.AndroidEnvironmentInteractor
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.google.android.gms.common.GoogleApiAvailability
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
object ProvideAppModule {

    @Provides
    @Reusable
    @JvmStatic
    fun providesSchedulersPackage() = SchedulersPackage(Schedulers.io(), AndroidSchedulers.mainThread())

    @Provides
    @Reusable
    @JvmStatic
    fun providesEnvironment(context: Context, googleApiAvailability: GoogleApiAvailability): EnvironmentInteractor =
        AndroidEnvironmentInteractor(context, googleApiAvailability)

    @Provides
    @Reusable
    @JvmStatic
    fun providesGoogleApiAvailability(): GoogleApiAvailability = GoogleApiAvailability.getInstance()

    @Provides
    @Reusable
    @JvmStatic
    fun providesTimeProvider(): () -> Long = { System.currentTimeMillis() }

    @Provides
    @Singleton
    @JvmStatic
    fun providesAntiSpam(timeProvider: () -> Long) = AntiSpam(timeProvider)
}