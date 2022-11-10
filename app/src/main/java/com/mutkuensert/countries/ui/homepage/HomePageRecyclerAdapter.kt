package com.mutkuensert.countries.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.countries.data.CountriesDataModel
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.databinding.SingleItemBinding
import com.mutkuensert.countries.ui.ItemClickListener

class HomePageRecyclerAdapter(private val clickListener: ItemClickListener): ListAdapter<CountriesDataModel, HomePageRecyclerAdapter.ViewHolder>(CountriesDataModelDiffCallback) {
    class ViewHolder(val binding: SingleItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = getItem(position).name
        holder.binding.saveButton.setOnClickListener {
            val newSavedCountry = SavedCountryModel(getItem(position).code!!, getItem(position).name!!)
            clickListener.onItemClick(newSavedCountry)
        }
    }

    object CountriesDataModelDiffCallback: DiffUtil.ItemCallback<CountriesDataModel>(){
        override fun areItemsTheSame(
            oldItem: CountriesDataModel,
            newItem: CountriesDataModel
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CountriesDataModel,
            newItem: CountriesDataModel
        ): Boolean {
            return oldItem.name == newItem.name
        }

    }
}