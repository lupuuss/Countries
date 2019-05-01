package com.github.lupuuss.countries.model.dataclass

class ShortCountry(
    val name: String,
    val flag: String
) {
    override fun toString(): String {
        return "[$name, $flag]"
    }
}