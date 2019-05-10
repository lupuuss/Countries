package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.ui.modules.details.DetailsActivity
import com.github.lupuuss.countries.ui.modules.details.DetailsPresenter
import com.github.lupuuss.countries.ui.modules.main.MainActivity
import com.github.lupuuss.countries.ui.modules.main.MainPresenter
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope

@Scope
annotation class TestMainScope

@Scope
annotation class TestDetailsScope

@Module
object TestMainActivityModule {
    @Provides
    @TestMainScope
    @JvmStatic
    fun providesMainPresenter(): MainPresenter = mock { }
}

@Module
object TestDetailsActivityModule {
    @Provides
    @TestDetailsScope
    @JvmStatic
    fun providesDetailsPresenter(): DetailsPresenter = mock { }
}

@Module
abstract class TestActivityBuilder {

    @TestMainScope
    @ContributesAndroidInjector(modules = [TestMainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @TestDetailsScope
    @ContributesAndroidInjector(modules = [TestDetailsActivityModule::class])
    abstract fun bindDetailsActivity(): DetailsActivity
}