package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.TestCountriesApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Scope

@Scope
annotation class TestAppComponentScope

@TestAppComponentScope
@Component(modules = [
    TestNetworkingModule::class,
    TestActivityBuilder::class,
    AndroidInjectionModule::class,
    TestAndroidModule::class,
    TestAppModule::class
])
interface TestAppComponent : AndroidInjector<TestCountriesApp>