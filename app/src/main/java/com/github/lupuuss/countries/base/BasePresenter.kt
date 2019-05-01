package com.github.lupuuss.countries.base

abstract class BasePresenter<T : BaseView> {

    protected var view: T? = null

    open fun attachView(view: T) {
        this.view = view
    }

    open fun detachView() {
        this.view = null
    }
}