package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.ui.modules.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope

@Scope
annotation class MainScope

@Module
abstract class ActivityBuilder {

    @MainScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}