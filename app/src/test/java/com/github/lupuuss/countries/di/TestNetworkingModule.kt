package com.github.lupuuss.countries.di

import com.ahmadrosid.svgloader.SvgLoader
import com.github.lupuuss.countries.model.countries.CountriesApi
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides

@Module
object TestNetworkingModule {

    @Provides
    @TestAppComponentScope
    @JvmStatic
    fun providesCountriesApi(): CountriesApi = mock { }

    @Provides
    @TestAppComponentScope
    @JvmStatic
    fun providesGson(): Gson = mock { }

    @Provides
    @TestAppComponentScope
    @JvmStatic
    fun providesCountriesManager(): CountriesManager = mock {}

    @Provides
    @TestAppComponentScope
    @JvmStatic
    fun providesSvgLoader(): SvgLoader = mock { }
}