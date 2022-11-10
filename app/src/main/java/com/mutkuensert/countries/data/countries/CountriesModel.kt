package com.mutkuensert.countries.data.countries

data class CountriesModel(
    val data: List<CountriesDataModel>,
    val links: List<CountriesLinksModel>,
    val metadata: CountriesMetadataModel
)
