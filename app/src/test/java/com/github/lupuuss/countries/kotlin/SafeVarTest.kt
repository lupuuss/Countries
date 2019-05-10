package com.github.lupuuss.countries.kotlin

import org.junit.Test

import org.junit.Assert.*

class SafeVarTest {

    private val safeVar = SafeVar<Int>()

    @Test
    fun getValue_shouldReturnNullAtStart() {

        assertEquals(null, safeVar.value)
    }

    @Test
    fun getValue_shouldReturnProperValueAfterSet() {

        assertEquals(null, safeVar.value)
        safeVar.value = 22
        assertEquals(22, safeVar.value)
    }


    @Test
    fun setValue_shouldNotCallUsage_whenNull() {

        var usageState = false
        safeVar.use {
            usageState = true
        }

        assertEquals(false, usageState)
    }

    @Test
    fun setValue_shouldCallUsageWithProperArgument_whenNotNull() {

        var usageState = false
        var arg = 0
        safeVar.use {
            usageState = true
            arg = it
        }

        safeVar.value = 2

        assertEquals(true, usageState)
        assertEquals(2, arg)
    }

    @Test
    fun use_shouldCallUsageInstantlyWithProperArg_whenNotNull() {
        var arg = 0
        safeVar.value = 2
        safeVar.use {
            arg = it
        }

        assertEquals(2, arg)
    }

    @Test
    fun use_shouldNotCallUsageAndPostponeIt_whenNull() {

        var arg = 0
        safeVar.use {
            arg = 2
        }
        assertEquals(0, arg)

        safeVar.value = 2

        assertEquals(2, arg)
    }
}