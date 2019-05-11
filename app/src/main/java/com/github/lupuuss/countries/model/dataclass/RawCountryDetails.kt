package com.github.lupuuss.countries.model.dataclass
import com.google.gson.annotations.SerializedName

@Suppress("unused", "MemberVisibilityCanBePrivate", "SpellCheckingInspection")
class RawCountryDetails(
    val name: String?,
    val alpha2Code: String?,
    val alpha3Code: String?,
    val area: Double?,
    val capital: String?,
    val cioc: String?,
    val demonym: String?,
    val flag: String?,
    val gini: Double?,
    val nativeName: String?,
    val numericCode: String?,
    val population: Int?,
    val region: String?,
    val subregion: String?,
    val translations: Translations?,
    val altSpellings: List<String>,
    val borders: List<String>,
    val callingCodes: List<String>,
    val currencies: List<Currency>,
    val languages: List<Language>,
    val latlng: List<Double>,
    val regionalBlocs: List<RegionalBloc>,
    val timezones: List<String>,
    val topLevelDomain: List<String>

)  {
    class Language(
        @SerializedName("iso639_1")
        val iso6391: String,
        @SerializedName("iso639_2")
        val iso6392: String,
        val name: String,
        val nativeName: String
    )

    class Translations(
        val br: String,
        val de: String,
        val es: String,
        val fa: String,
        val fr: String,
        val hr: String,
        val it: String,
        val ja: String,
        val nl: String,
        val pt: String
    )

    class RegionalBloc(
        val acronym: String,
        val name: String,
        val otherAcronyms: List<Any>,
        val otherNames: List<Any>
    )

    class Currency(
        val code: String,
        val name: String,
        val symbol: String
    ) {
        fun toCurrencyString(): String {

            if (symbol == "") {
                return name
            }

            return "$name ($symbol)"
        }
    }

    constructor(details: RawCountryDetails, borders: List<String>): this(
        details.name, details.alpha2Code, details.alpha3Code, details.area, details.capital, details.cioc,
        details.demonym, details.flag, details.gini, details.nativeName, details.numericCode, details.population,
        details.region, details.subregion, details.translations, details.altSpellings, borders,
        details.callingCodes, details.currencies, details.languages, details.latlng, details.regionalBlocs,
        details.timezones, details.topLevelDomain
    )
}