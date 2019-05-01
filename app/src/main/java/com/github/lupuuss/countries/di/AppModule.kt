package com.github.lupuuss.countries.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    @AppComponentScope
    abstract fun bindsContext(application: Application): Context
}