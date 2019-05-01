package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.CountriesApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Scope

@Scope
annotation class AppComponentScope

@AppComponentScope
@Component(modules = [
    NetworkingModule::class,
    ActivityBuilder::class,
    AndroidInjectionModule::class,
    AppModule::class,
    AndroidModule::class
])
interface AppComponent : AndroidInjector<CountriesApp>