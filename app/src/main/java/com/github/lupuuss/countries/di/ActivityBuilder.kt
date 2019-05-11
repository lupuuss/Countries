package com.github.lupuuss.countries.di

import com.github.lupuuss.countries.ui.modules.details.DetailsActivity
import com.github.lupuuss.countries.ui.modules.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindDetailsActivity(): DetailsActivity
}