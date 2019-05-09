package com.github.lupuuss.countries.base

interface BaseView {

    enum class Message {
        GOOGLE_MAPS_UNAVAILABLE
    }

    fun postString(msg: String)
    fun postMessage(message: Message)
}