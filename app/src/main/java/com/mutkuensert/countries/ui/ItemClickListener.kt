package com.mutkuensert.countries.ui

import com.mutkuensert.countries.data.SavedCountryModel

interface ItemClickListener {
    fun onItemClick(country: SavedCountryModel)
}