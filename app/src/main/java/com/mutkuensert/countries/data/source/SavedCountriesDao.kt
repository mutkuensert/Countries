package com.mutkuensert.countries.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mutkuensert.countries.data.SavedCountryModel

@Dao
interface SavedCountriesDao{
    @Query("SELECT * FROM SavedCountries")
    suspend fun getAll(): List<SavedCountryModel>

    @Delete
    suspend fun delete(savedCountryModel: SavedCountryModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(savedCountryModel: SavedCountryModel)

    @Query("SELECT EXISTS(SELECT * FROM SavedCountries WHERE country_name = :countryName)")
    suspend fun doesCountryExist(countryName: String): Boolean
}
