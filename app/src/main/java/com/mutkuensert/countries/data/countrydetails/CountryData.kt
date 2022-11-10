package com.mutkuensert.countries.data.countrydetails

data class CountryData(
    val capital: String?,
    val code: String?,
    val callingCode: String?,
    val currencyCodes: List<String>?,
    val flagImageUri: String?,
    val name: String?,
    val numRegions: Int?,
    val wikiDataId: String?
)
