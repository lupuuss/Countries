package com.github.lupuuss.countries.di

import dagger.Component
import javax.inject.Scope

@Scope
annotation class MainComponentScope

@MainComponentScope
@Component(modules = [NetworkingModule::class])
interface MainComponent {
}