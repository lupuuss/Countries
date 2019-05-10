package com.github.lupuuss.countries

import org.junit.Assert
import org.junit.Test
import java.text.NumberFormat

class UtilsTest {

    @Test
    fun flatList_shouldReturnEmptyString_whenEmptyList() {

        Assert.assertEquals("", flatList(listOf<Any>()) { it.toString() })
    }

    @Test
    fun flatList_shouldReturnProperlyFormattedString_whenNotEmptyList() {

        val sampleList = listOf("one", "two", "tree")

        Assert.assertEquals("ONE, TWO, TREE", flatList(sampleList) { it.toUpperCase() })
    }

    @Test
    fun formatAny_shouldReturnEmptyString_whenNull() {

        Assert.assertEquals("", formatAny(null, "anything"))
    }

    @Test
    fun formatAny_shouldReturnSameValueAsNumberFormat_whenNoAppend() {

        val number = 22_000

        Assert.assertEquals(NumberFormat.getInstance().format(number), formatAny(number))
    }

    @Test
    fun formatAny_shouldReturnSameValueAsNumberFormatAndAppend_whenAppend() {

        val number = 22_000

        Assert.assertEquals(
            NumberFormat.getInstance().format(number) + "Append",
            formatAny(number, "Append")
        )
    }

    @Test
    fun boldQuery_shouldReturnIdentity_whenNoQuery() {

        Assert.assertEquals("Sample text...", "Sample text...".boldQuery(""))
    }

    @Test
    fun boldQuery_shouldReturnProperlyBoldText() {

        Assert.assertEquals("Sampl<b>e</b> t<b>e</b>xt...", "Sample text...".boldQuery("e"))
        Assert.assertEquals(
            "<b>Simple</b> text with <b>simple</b> moral.",
            "Simple text with simple moral.".boldQuery("simple")
        )
    }
}