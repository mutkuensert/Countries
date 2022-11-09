package com.mutkuensert.countries.ui.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mutkuensert.countries.data.source.RequestService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(private val requestService: RequestService): ViewModel() {

    fun requestCountries(){
        viewModelScope.launch(Dispatchers.IO) {
            requestService.getCountries().also {
                if(it.isSuccessful){
                    println(it.body())
                }
            }
        }
    }
}