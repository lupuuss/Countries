package com.github.lupuuss.countries

import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
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

/**
 * Calculates zoom by interpolation for some country samples
 */
fun calculateZoom(rawCountryDetails: RawCountryDetails): Float {

    val area = rawCountryDetails.area

    return when {
        area == 0.0 -> 7f
        area <= 500 -> (11.0024 - 0.00534691 * area).toFloat()
        area in 500.0..28_000.0 -> (8f - 0.000055 * area).toFloat()
        area in 28_000.0..1_000_000.0 -> (6f - 2.0E-6 * area).toFloat()
        else -> (4.625 - 1.25E-7 * area).toFloat()

    }
}