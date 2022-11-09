package com.mutkuensert.countries.data

data class CountriesModel(
    val data: List<CountriesDataModel>,
    val links: List<CountriesLinksModel>,
    val metadata: CountriesMetadataModel
)
