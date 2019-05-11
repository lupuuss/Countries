package com.github.lupuuss.countries.di

import com.ahmadrosid.svgloader.SvgLoader
import com.github.lupuuss.countries.CountriesApp
import com.github.lupuuss.countries.model.countries.BasicCountriesManager
import com.github.lupuuss.countries.model.countries.CountriesApi
import com.github.lupuuss.countries.model.countries.CountriesManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkingModule {

    @Provides
    @Singleton
    @JvmStatic
    fun providesCountriesApi(gson: Gson): CountriesApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(CountriesApp.API_URL)
            .build()
            .create(CountriesApi::class.java)

    @Provides
    @Reusable
    @JvmStatic
    fun providesGson(): Gson = Gson()

    @Provides
    @Singleton
    @JvmStatic
    fun providesCountriesManager(countriesApi: CountriesApi, schedulersPackage: SchedulersPackage): CountriesManager =
        BasicCountriesManager(countriesApi, schedulersPackage)

    @Provides
    @Reusable
    @JvmStatic
    fun providesSvgLoader() = SvgLoader.pluck()
}