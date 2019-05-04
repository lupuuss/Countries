package com.github.lupuuss.countries.model.dataclass

import java.text.NumberFormat

class CountryDetails(
    val name: String,
    val area: Double,
    val capital: String,
    val demonym: String,
    val flag: String,
    val gini: Double,
    val nativeName: String,
    val numericCode: String,
    val population: Int,
    val region: String,
    val subregion: String
    ) {

    val formattedPopulation: String
            get() = NumberFormat.getInstance().format(population)
    val formattedArea: String
            get() = NumberFormat.getInstance().format(area) + " km2"

    val formattedGini: String
            get() = NumberFormat.getInstance().format(gini) + "%"
}