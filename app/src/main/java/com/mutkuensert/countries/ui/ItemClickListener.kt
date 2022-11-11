package com.mutkuensert.countries.ui

import com.mutkuensert.countries.data.SavedCountryModel

interface ItemClickListener {
    fun onItemClickSave(savedCountryModel: SavedCountryModel, position: Int)
    fun onItemClickDelete(savedCountryModel: SavedCountryModel, position: Int)
    fun onItemNameClick(position: Int)
}