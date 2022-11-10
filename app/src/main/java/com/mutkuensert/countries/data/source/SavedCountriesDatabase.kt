package com.mutkuensert.countries.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mutkuensert.countries.data.SavedCountryModel

@Database(entities = [SavedCountryModel::class], version = 1)
abstract class SavedCountriesDatabase: RoomDatabase() {
    abstract fun savedCountriesDao(): SavedCountriesDao
}