package com.mutkuensert.countries.data.countries

data class CountriesDataAndExistenceInDatabaseModel(
    val data: CountriesDataModel,
    var existenceInDatabase: Boolean = false
)
