package com.github.lupuuss.countries.ui.modules.main

import com.github.lupuuss.countries.base.BaseView

interface MainView : BaseView {
    fun postString(msg: String)
}