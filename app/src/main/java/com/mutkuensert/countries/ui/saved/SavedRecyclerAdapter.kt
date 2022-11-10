package com.mutkuensert.countries.ui.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.databinding.SingleItemBinding

class SavedRecyclerAdapter: ListAdapter<SavedCountryModel, SavedRecyclerAdapter.ViewHolder>(SavedCountryModelDiffCallback) {
    class ViewHolder(val binding: SingleItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = getItem(position).countryName
    }

    object SavedCountryModelDiffCallback: DiffUtil.ItemCallback<SavedCountryModel>(){
        override fun areItemsTheSame(
            oldItem: SavedCountryModel,
            newItem: SavedCountryModel
        ): Boolean {
            return oldItem.countryId == newItem.countryId
        }

        override fun areContentsTheSame(
            oldItem: SavedCountryModel,
            newItem: SavedCountryModel
        ): Boolean {
            return oldItem.countryName == newItem.countryName
        }

    }
}