package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.model.countries.CountriesManager
import com.github.lupuuss.countries.model.environment.EnvironmentInteractor
import com.github.lupuuss.countries.ui.modules.details.DetailsActivity
import com.github.lupuuss.countries.ui.modules.details.DetailsPresenter
import com.github.lupuuss.countries.ui.modules.main.MainActivity
import com.github.lupuuss.countries.ui.modules.main.MainPresenter
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope

@Scope
annotation class MainScope

@Scope
annotation class DetailsScope

@Module
object MainActivityModule {
    @Provides
    @MainScope
    @JvmStatic
    fun providesMainPresenter(countriesManager: CountriesManager, environment: EnvironmentInteractor): MainPresenter =
            MainPresenter(countriesManager, environment)
}

@Module
object DetailsActivityModule {

    @Provides
    @DetailsScope
    @JvmStatic
    fun providesDetailsPresenter(
        countriesManager: CountriesManager,
        environment: EnvironmentInteractor,
        gson: Gson
    ): DetailsPresenter = DetailsPresenter(countriesManager, gson, environment)
}

@Module
abstract class ActivityBuilder {

    @MainScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @DetailsScope
    @ContributesAndroidInjector(modules = [DetailsActivityModule::class])
    abstract fun bindDetailsActivity(): DetailsActivity
}