package com.mutkuensert.countries.ui.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.data.source.SavedCountriesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(private val databaseDao: SavedCountriesDao) : ViewModel() {

    private val _savedCountries = MutableLiveData<List<SavedCountryModel>>()
    val savedCountries get() = _savedCountries

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