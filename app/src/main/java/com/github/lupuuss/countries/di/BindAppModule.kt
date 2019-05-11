package com.github.lupuuss.countries.di

import android.content.Context
import com.github.lupuuss.countries.CountriesApp
import dagger.Binds
import dagger.Module

@Module
abstract class BindAppModule {

    @Binds
    abstract fun bindsContext(app: CountriesApp): Context
}