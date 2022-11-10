package com.mutkuensert.countries.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedCountries")
data class SavedCountryModel(
    @PrimaryKey val countryId: String,
    @ColumnInfo(name = "country_name") val countryName: String
)
