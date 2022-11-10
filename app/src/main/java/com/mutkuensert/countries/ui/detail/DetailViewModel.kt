package com.mutkuensert.countries.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.data.countrydetails.CountryData
import com.mutkuensert.countries.data.source.RequestService
import com.mutkuensert.countries.data.source.SavedCountriesDao
import com.mutkuensert.countries.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DetailViewModel"
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val requestService: RequestService,
    private val databaseDao: SavedCountriesDao): ViewModel() {
    private val _data = MutableLiveData<Resource<CountryData>>(Resource.standby(null))
    val data: LiveData<Resource<CountryData>> get() = _data

    private var _countryExists = MutableLiveData<Boolean>(false)
    val countryExists get() = _countryExists

    fun requestCountryDetail(countryCode: String){
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "requestCountryDetail(), countryCode Parameter: $countryCode")
            requestService.getCountryDetail(countryCode).also { response ->
                if(response.isSuccessful){
                    val body = response.body()
                    if(body?.data != null){
                        _data.postValue(Resource.success(body.data))
                        Log.i(TAG, "requestCountryDetail(): ${body.data}")
                    }else{
                        _data.postValue(Resource.error("Response body is empty.", null))
                    }
                }
            }
        }
    }

    fun doesCountryExistInUsersDatabase(countryName: String){
        viewModelScope.launch(Dispatchers.IO) {
            _countryExists.postValue(databaseDao.doesCountryExist(countryName))
        }
    }

    fun saveData(savedCountryModel: SavedCountryModel){
        viewModelScope.launch(Dispatchers.IO) {
            databaseDao.insertAll(savedCountryModel)
            _countryExists.postValue(true)
        }
    }

    fun deleteSavedData(savedCountryModel: SavedCountryModel){
        viewModelScope.launch(Dispatchers.IO) {
            databaseDao.delete(savedCountryModel)
            _countryExists.postValue(false)
        }
    }
}