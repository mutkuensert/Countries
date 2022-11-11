package com.mutkuensert.countries.data.countries

data class CountriesDataAndExistenceInDatabaseModel(
    val data: CountriesDataModel,
    var existence: Boolean = false
)
