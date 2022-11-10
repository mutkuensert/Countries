package com.mutkuensert.countries.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.countries.data.countrydetails.CountryData
import com.mutkuensert.countries.data.source.RequestService
import com.mutkuensert.countries.util.BASE_URL
import com.mutkuensert.countries.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DetailViewModel"
@HiltViewModel
class DetailViewModel @Inject constructor(private val requestService: RequestService): ViewModel() {
    private val _data = MutableLiveData<Resource<CountryData>>(Resource.standby(null))
    val data get() = _data

    fun requestCountryDetail(countryCode: String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "requestCountryDetail(), countryCode Parameter: $countryCode")
            requestService.getCountryDetail(countryCode).also { response ->
                if(response.isSuccessful){
                    val body = response.body()
                    if(body != null && body.data != null){
                        _data.postValue(Resource.success(body.data))
                        Log.i(TAG, "requestCountryDetail(): ${body.data}")
                    }else{
                        _data.postValue(Resource.error("Response body is empty.", null))
                    }
                }
            }
        }
    }
}