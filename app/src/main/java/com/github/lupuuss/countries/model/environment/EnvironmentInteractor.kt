package com.github.lupuuss.countries.model.environment

interface MapStatus {

    enum class Code {
        NEEDS_USER_ACTIONS, UNAVAILABLE, AVAILABLE
    }

    val statusMessage: String?
    val statusCode: Code

    fun isAvailable(): Boolean {
        return statusCode == Code.AVAILABLE
    }
}

interface EnvironmentInteractor {

    fun isNetworkAvailable(): Boolean
    fun isMapAvailable(): MapStatus
    fun getCountriesBoxesJson(): String
}