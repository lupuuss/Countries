package com.github.lupuuss.countries.kotlin

class AntiSpam(private val timeProvider: () -> Long) {

    private val lastClick: MutableMap<String, Long> = mutableMapOf()

    fun doAction(identifier: String, minDeltaTime: Long, action: () -> Unit) {

        val lastTime = lastClick.getOrElse(identifier) { 0L }
        val time = timeProvider()

        if ( time - lastTime > minDeltaTime) {

            lastClick[identifier] = time
            action()
        }
    }

    companion object {

        const val STANDARD_DELTA = 1000L
    }
}