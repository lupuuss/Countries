package com.github.lupuuss.countries.kotlin

import org.junit.Test

import org.junit.Assert.*

class AntiSpamTest {

    private val times = mutableListOf(0L, 1001L, 1500L)
    private val antiSpam = AntiSpam { times.removeAt(0)}

    @Test
    fun doAction_shouldAlwaysExecuteAction_whenFirstAction() {

        var actionDone = false
        antiSpam.doAction("any", AntiSpam.STANDARD_DELTA) {
            actionDone = true
        }

        assertEquals(true, actionDone)
    }

    @Test
    fun doAction_shouldExecuteAction_whenDeltaBiggerThanMinDelta() {

        var actionDone = false
        antiSpam.doAction("any", AntiSpam.STANDARD_DELTA) {}
        antiSpam.doAction("any", AntiSpam.STANDARD_DELTA) {
            actionDone = true
        }

        assertEquals(true, actionDone)
    }

    @Test
    fun doAction_shouldNotExecuteAction_whenDeltaLessThanMinDelta() {

        var actionDone = false
        antiSpam.doAction("any", AntiSpam.STANDARD_DELTA) {}
        antiSpam.doAction("any", AntiSpam.STANDARD_DELTA) {}
        antiSpam.doAction("any", AntiSpam.STANDARD_DELTA) {
            actionDone = true
        }

        assertEquals(false, actionDone)
    }
}