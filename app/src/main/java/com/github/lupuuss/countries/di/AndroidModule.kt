package com.github.lupuuss.countries.di

import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
object AndroidModule {

    @Provides
    @AppComponentScope
    @JvmStatic
    fun providesSchedulersPackage() = SchedulersPackage(Schedulers.io(), AndroidSchedulers.mainThread())
}