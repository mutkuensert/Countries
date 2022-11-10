package com.mutkuensert.countries

import android.util.Log
import com.mutkuensert.countries.data.countries.CountriesModel
import com.mutkuensert.countries.data.source.RequestService
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "RequestServiceTest"
@HiltAndroidTest
class RequestServiceTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var requestService: RequestService
    private lateinit var response: Response<CountriesModel>

    @Before
    fun initRequestService(){
        hiltRule.inject()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun getRequestServiceResponse() = runTest {
        response = requestService.getCountries()
    }

    /*@Test
    fun errorBodyIsNull(){
        assert(response.errorBody() == null)
    }

    @Test
    fun responseBodyIsNotNull(){
        assert(response.body() != null)
    }*/

    @Test
    fun httpCodeIs200(){
        assert(response.code() == 200)
    }

    @After
    fun after(){
        Log.i(TAG, "Body: ${response.body()}")
        Log.i(TAG, "Code: ${response.code()}")
    }
}