package com.github.lupuuss.countries.model.dataclass

open class ShortCountry(
    val name: String,
    val alpha3Code: String
) {
    override fun toString(): String {
        return "[$name]"
    }
}