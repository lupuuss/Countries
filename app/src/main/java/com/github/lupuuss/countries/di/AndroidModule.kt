package com.github.lupuuss.countries.di

import android.content.Context
import com.github.lupuuss.countries.model.environment.AndroidEnvironmentInteractor
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
object AndroidModule {

    @Provides
    @AppComponentScope
    @JvmStatic
    fun providesSchedulersPackage() = SchedulersPackage(Schedulers.io(), AndroidSchedulers.mainThread())

    @Provides
    @AppComponentScope
    @JvmStatic
    fun providesEnvironment(context: Context): EnvironmentInteractor =
        AndroidEnvironmentInteractor(context)
}