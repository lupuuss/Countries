package com.github.lupuuss.countries.model.dataclass

class BasicCountryInfo(
    val name: String,
    val flag: String
) {
    override fun toString(): String {
        return "[$name, $flag]"
    }
}