package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.ui.modules.details.DetailsActivity
import com.github.lupuuss.countries.ui.modules.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope

@Scope
annotation class MainScope

@Scope
annotation class DetailsScope

@Module
abstract class ActivityBuilder {

    @MainScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @DetailsScope
    @ContributesAndroidInjector
    abstract fun bindDetailsActivity(): DetailsActivity
}