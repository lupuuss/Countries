package com.github.lupuuss.countries.model.dataclass

class ShortCountry(
    val name: String,
    val alpha3Code: String
) {
    override fun toString(): String {
        return "[$name]"
    }
}