package com.github.lupuuss.countries

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.github.lupuuss.countries.model.dataclass.RawCountryDetails
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.regex.Pattern

fun <T> flatList(list: List<T>, transformer: (T) -> String): String {

    if (list.isEmpty() || list.first() == "") return ""

    val stringBuilder = StringBuilder()

    for (i in 0..list.size - 2) {
        stringBuilder.append(transformer(list[i]))
        stringBuilder.append(", ")
    }

    stringBuilder.append(transformer(list.last()))
    return stringBuilder.toString()
}

fun formatAny(any: Any?, append: String = ""): String {

    return if (any != null)
        NumberFormat.getInstance().format(any) + append
    else ""
}

@Suppress("DEPRECATION")
fun String.htmlToSpanned(): Spanned {

    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
        Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }
}

fun String.boldQuery(query: String): String {

    if (query == "") return this

    val strB = StringBuilder()
    this.split(Pattern.compile("((?<=$query)|(?=$query))", Pattern.CASE_INSENSITIVE)).forEach {

        if (it.contains(query, true)) {

            strB.append("<b>")
            strB.append(it)
            strB.append("</b>")

        } else {

            strB.append(it)
        }

    }
    return strB.toString()
}

/**
 * Calculates zoom by interpolation for some country samples
 */
fun calculateZoom(rawCountryDetails: RawCountryDetails): Float {

    val area = rawCountryDetails.area

    return when {
        area == null || area == 0.0 -> 7f
        area <= 500 -> (11.0024 - 0.00534691 * area).toFloat()
        area in 500.0..28_000.0 -> (8f - 0.000055 * area).toFloat()
        area in 28_000.0..1_000_000.0 -> (6f - 2.0E-6 * area).toFloat()
        else -> (4.625 - 1.25E-7 * area).toFloat()

    }
}