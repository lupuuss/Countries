package com.github.lupuuss.countries

import java.lang.StringBuilder
import java.text.NumberFormat

fun <T> flatList(list: List<T>, transformer: (T) -> String): String {

    if (list.isEmpty()) return ""

    val stringBuilder = StringBuilder()

    for (i in 0..list.size - 2) {
        stringBuilder.append(transformer(list[i]))
        stringBuilder.append(", ")
    }

    stringBuilder.append(transformer(list.last()))
    return stringBuilder.toString()
}

fun formatAny(any: Any): String = NumberFormat.getInstance().format(any)

