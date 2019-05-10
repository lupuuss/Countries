package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.kotlin.AntiSpam
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @Provides
    @AppComponentScope
    @JvmStatic
    fun providesTimeProvider(): () -> Long = { System.currentTimeMillis() }

    @Provides
    @AppComponentScope
    @JvmStatic
    fun providesAntiSpam(timeProvider: () -> Long) = AntiSpam(timeProvider)
}