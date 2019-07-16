package com.github.lupuuss.countries.model.dataclass

data class BoundingBox (
    val sw: Coord,
    val ne: Coord
)

data class Coord(
    val lat: Double,
    val lon: Double
)