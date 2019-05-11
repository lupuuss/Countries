package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.kotlin.AntiSpam
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides

@Module
object TestAppModule {

    @Provides
    @TestAppComponentScope
    @JvmStatic
    fun providesTimeProvider(): () -> Long = mock {}

    @Provides
    @TestAppComponentScope
    @JvmStatic
    fun providesAntiSpam() = mock<AntiSpam> {
        on { doAction(any(), any(), any()) }.then { (it.getArgument(2) as ()->Unit).invoke() }
    }
}