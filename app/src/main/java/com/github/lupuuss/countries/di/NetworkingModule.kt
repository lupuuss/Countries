package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.COUNTRIES_API_URL
import com.github.lupuuss.countries.model.countries.CountriesApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkingModule {

    @Provides
    @MainComponentScope
    fun providesCountriesApi(gson: Gson): CountriesApi =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(COUNTRIES_API_URL)
            .build()
            .create(CountriesApi::class.java)

    @Provides
    @MainComponentScope
    fun providesGson(): Gson = Gson()
}