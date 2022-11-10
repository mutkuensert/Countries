package com.mutkuensert.countries.ui.homepage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.countries.data.CountriesDataModel
import com.mutkuensert.countries.data.CountriesLinksModel
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.data.source.RequestService
import com.mutkuensert.countries.data.source.SavedCountriesDao
import com.mutkuensert.countries.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SharedViewModel"
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val requestService: RequestService,
    private val databaseDao: SavedCountriesDao): ViewModel() {

    private val _data = MutableLiveData<Resource<List<CountriesDataModel>>>(Resource.standby(null))
    val data get() = _data
    private var nextPageUrl: String? = null

    private val _savedCountries = MutableLiveData<List<SavedCountryModel>>()
    val savedCountries get() = _savedCountries

    fun requestCountries(){
        viewModelScope.launch(Dispatchers.IO) {
            _data.postValue(Resource.loading(null))
            requestService.getCountries().also { response->
                val body = response.body()
                if(response.isSuccessful && body != null){
                    _data.postValue(Resource.success(body.data))
                    Log.i(TAG, "requestCountries(): ${body}")
                    setNextPageUrlIfExistsOrSetItNull(body.links)

                }else{
                    _data.postValue(Resource.error("Error", null))
                }
            }
        }
    }

    fun getCountriesNextPage(){
        viewModelScope.launch(Dispatchers.IO) {
            val oldData = data.value?.data
            _data.postValue(Resource.loading(oldData))
            if(nextPageUrl != null) {
                requestService.getCountriesNextPage(nextPageUrl!!).also { response->
                    val body = response.body()
                    if(response.isSuccessful && body != null){

                        Log.i(TAG, "getCountriesNextPage(): ${body}")
                        val newData = _data.value!!.data!! + body.data
                        _data.postValue(Resource.success(newData))
                        setNextPageUrlIfExistsOrSetItNull(body.links)

                    }else{
                        _data.postValue(Resource.error("Error", null))
                    }
                }
            }else{
                _data.postValue(Resource.error("All countries have been listed", null))
            }
        }
    }

    private fun setNextPageUrlIfExistsOrSetItNull(links: List<CountriesLinksModel>){
        var nextPageExists = false
        for(i in links){
            if(i.rel == "next") {
                nextPageUrl = "https://wft-geo-db.p.rapidapi.com" + i.href
                nextPageExists = true
                Log.i(TAG, "nextPageUrl: ${nextPageUrl}")
            }
        }
        if (!nextPageExists) nextPageUrl = null
    }

    fun getAllSavedDataAndRefresh(){
        viewModelScope.launch(Dispatchers.IO) {
            _savedCountries.postValue(databaseDao.getAll())
        }
    }

    fun saveData(savedCountryModel: SavedCountryModel){
        viewModelScope.launch(Dispatchers.IO) {
            databaseDao.insertAll(savedCountryModel)
        }
    }

    fun deleteSavedData(savedCountryModel: SavedCountryModel){
        viewModelScope.launch(Dispatchers.IO) {
            databaseDao.delete(savedCountryModel)
        }
    }
}