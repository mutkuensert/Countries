package com.mutkuensert.countries.data.source

import com.mutkuensert.countries.data.CountriesModel
import com.mutkuensert.countries.util.API_KEY
import com.mutkuensert.countries.util.X_RAPID_API_HOST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface RequestService {

    @Headers("X-RapidAPI-Key: $API_KEY", "X-RapidAPI-Host: $X_RAPID_API_HOST")
    @GET("countries")
    suspend fun getCountries(): Response<CountriesModel>
}