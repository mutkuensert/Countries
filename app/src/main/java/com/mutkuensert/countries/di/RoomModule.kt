package com.mutkuensert.countries.di

import android.app.Application
import androidx.room.Room
import com.mutkuensert.countries.data.source.SavedCountriesDao
import com.mutkuensert.countries.data.source.SavedCountriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): SavedCountriesDatabase{
        return Room.databaseBuilder(
            application,
            SavedCountriesDatabase::class.java,
            "saved-countries-database").build()
    }

    @Singleton
    @Provides
    fun providesDatabaseDao(database: SavedCountriesDatabase): SavedCountriesDao{
        return database.savedCountriesDao()
    }
}