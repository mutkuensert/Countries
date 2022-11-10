package com.mutkuensert.countries.ui

import com.mutkuensert.countries.data.SavedCountryModel

interface ItemClickListener {
    fun onItemClickSave(savedCountryModel: SavedCountryModel)
    fun onItemClickDelete(savedCountryModel: SavedCountryModel)
}