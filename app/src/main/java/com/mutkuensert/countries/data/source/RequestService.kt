package com.mutkuensert.countries.data.source

import com.mutkuensert.countries.data.CountriesModel
import com.mutkuensert.countries.util.API_KEY
import com.mutkuensert.countries.util.BASE_URL
import com.mutkuensert.countries.util.X_RAPID_API_HOST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface RequestService {

    @Headers("X-RapidAPI-Key: $API_KEY", "X-RapidAPI-Host: $X_RAPID_API_HOST")
    @GET("countries")
    suspend fun getCountries(
        @Query("limit") limit: Int = 10
    ): Response<CountriesModel>

    @Headers("X-RapidAPI-Key: $API_KEY", "X-RapidAPI-Host: $X_RAPID_API_HOST")
    @GET
    suspend fun getCountriesNextPage(
        @Url url: String
    ): Response<CountriesModel>
}