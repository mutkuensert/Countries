package com.mutkuensert.countries.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.countries.data.countries.CountriesDataModel
import com.mutkuensert.countries.data.countries.CountriesLinksModel
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.data.countries.CountriesDataAndExistenceInDatabaseModel
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

    private val _data = MutableLiveData<Resource<List<CountriesDataAndExistenceInDatabaseModel>>>(Resource.standby(null))
    val data: LiveData<Resource<List<CountriesDataAndExistenceInDatabaseModel>>> get() = _data
    private var nextPageUrl: String? = null

    private val _savedCountries = MutableLiveData<List<SavedCountryModel>>(mutableListOf())
    val savedCountries: LiveData<List<SavedCountryModel>> get() = _savedCountries

    fun requestCountries(){
        viewModelScope.launch(Dispatchers.IO) {
            _data.postValue(Resource.loading(null))
            requestService.getCountries().also { response->
                val body = response.body()
                if(response.isSuccessful && body?.data != null){
                    val dataWillBePosted = checkExistenceInDatabaseAndMatchReceivedDataWithSavedData(body.data!!)
                    _data.postValue(Resource.success(dataWillBePosted))
                    Log.i(TAG, "requestCountries(): ${body}")
                    setNextPageUrlIfExistsOrSetItNull(body!!.links)

                }else{
                    _data.postValue(Resource.error("Error", null))
                }
            }
        }
    }

    fun getCountriesNextPage(){
        viewModelScope.launch(Dispatchers.IO) {
            if(nextPageUrl != null) {
                val oldData = data.value!!.data!! //If nextPageUrl is not null, _data is not null either.
                _data.postValue(Resource.loading(oldData))

                requestService.getCountriesNextPage(nextPageUrl!!).also { response->
                    val body = response.body()
                    if(response.isSuccessful && body?.data != null){

                        Log.i(TAG, "getCountriesNextPage() received body: ${body}")
                        val newDataMatchedWithSavedDatas = checkExistenceInDatabaseAndMatchReceivedDataWithSavedData(body.data!!)
                        val dataWillBePosted = _data.value!!.data!!.plus(newDataMatchedWithSavedDatas)
                        _data.postValue(Resource.success(dataWillBePosted))
                        setNextPageUrlIfExistsOrSetItNull(body!!.links)

                    }else{
                        _data.postValue(Resource.error("Error", null))
                    }
                }
            }else{
                _data.postValue(Resource.error("All countries have been listed", null))
            }
        }
    }

    private fun setNextPageUrlIfExistsOrSetItNull(links: List<CountriesLinksModel>?){
        var nextPageExists = false
        links?.let {
            for(i in it){
                if(i.rel == "next") {
                    nextPageUrl = "https://wft-geo-db.p.rapidapi.com" + i.href
                    nextPageExists = true
                    Log.i(TAG, "nextPageUrl: ${nextPageUrl}")
                }
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
            val data = _data.value!!.data!!
            for(i in data){
                if(i.data.name == savedCountryModel.countryName) {
                    i.existence = true
                    break
                }
            }
            getAllSavedDataAndRefresh()
            _data.postValue(Resource.success(data))
        }
    }

    fun deleteSavedData(savedCountryModel: SavedCountryModel){
        viewModelScope.launch(Dispatchers.IO) {
            databaseDao.delete(savedCountryModel)
            val data = _data.value!!.data!!
            for(i in data){
                if(i.data.name == savedCountryModel.countryName) {
                    i.existence = false
                    break
                }
            }
            getAllSavedDataAndRefresh()
            _data.postValue(Resource.success(data))
        }
    }

    private suspend fun checkExistenceInDatabaseAndMatchReceivedDataWithSavedData(receivedCountriesList: List<CountriesDataModel>): List<CountriesDataAndExistenceInDatabaseModel>{
        val newList = mutableListOf<CountriesDataAndExistenceInDatabaseModel>()
        for(i in receivedCountriesList){
            i.name?.let {
                if(databaseDao.doesCountryExist(it)){
                    newList.add(CountriesDataAndExistenceInDatabaseModel(i,true))
                }else{
                    newList.add(CountriesDataAndExistenceInDatabaseModel(i,false))
                }
            }
        }
        return newList
    }

    fun checkExistencesInDatabase(){
        viewModelScope.launch(Dispatchers.IO) {
            _data.value?.data?.let {
                val newList = mutableListOf<CountriesDataAndExistenceInDatabaseModel>()
                for(i in it){
                    i.data.name?.let {
                        if(databaseDao.doesCountryExist(it)){
                            newList.add(CountriesDataAndExistenceInDatabaseModel(i.data,true))
                        }else{
                            newList.add(CountriesDataAndExistenceInDatabaseModel(i.data,false))
                        }
                    }
                }
                _data.postValue(Resource.success(newList))
            }
        }
    }

}