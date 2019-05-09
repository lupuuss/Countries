package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.kotlin.AntiSpam
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @AppComponentScope
    fun providesTimeProvider() = { System.currentTimeMillis() }

    @Provides
    @AppComponentScope
    fun providesAntiSpam(timeProvider: () -> Long) = AntiSpam(timeProvider)
}