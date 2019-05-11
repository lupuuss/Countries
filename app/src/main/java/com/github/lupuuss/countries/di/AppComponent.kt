package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.CountriesApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkingModule::class,
    ActivityBuilder::class,
    AndroidInjectionModule::class,
    ProvideAppModule::class,
    BindAppModule::class
])
interface AppComponent : AndroidInjector<CountriesApp> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<CountriesApp>
}