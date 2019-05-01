package com.github.lupuuss.countries.di

import io.reactivex.Scheduler

class SchedulersPackage(
    val backScheduler: Scheduler,
    val frontScheduler: Scheduler
)