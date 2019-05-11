package com.github.lupuuss.countries.kotlin

class AntiSpam(private val timeProvider: () -> Long) {

    private val lastClick: MutableMap<String, Long> = mutableMapOf()

    fun doAction(identifier: String, minDeltaTime: Long, action: () -> Unit) {

        val lastTime = lastClick[identifier]
        val time = timeProvider()

        if (lastTime == null || time - lastTime > minDeltaTime) {

            action()
            lastClick[identifier] = time
        }
    }

    companion object {

        const val STANDARD_DELTA = 1000L
    }
}