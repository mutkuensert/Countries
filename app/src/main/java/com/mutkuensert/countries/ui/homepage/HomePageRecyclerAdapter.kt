package com.mutkuensert.countries.ui.homepage

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.countries.R
import com.mutkuensert.countries.data.countries.CountriesDataModel
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.databinding.SingleItemBinding
import com.mutkuensert.countries.ui.ItemClickListener
import com.mutkuensert.countries.ui.detail.DetailActivity

class HomePageRecyclerAdapter(private val clickListener: ItemClickListener): ListAdapter<CountriesDataModel, HomePageRecyclerAdapter.ViewHolder>(CountriesDataModelDiffCallback) {
    private val savedCountriesList = mutableListOf<SavedCountryModel>()

    class ViewHolder(val binding: SingleItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = getItem(position).name
        val country = SavedCountryModel(getItem(position).code!!, getItem(position).name!!)

        if(savedCountriesList.contains(country)){
            holder.binding.saveButton.setBackgroundResource(R.drawable.ic_saved_star)
        }

        holder.binding.saveButton.setOnClickListener {
            if(savedCountriesList.contains(country)){
                holder.binding.saveButton.setBackgroundResource(R.drawable.ic_unsaved_star)
                val countryWillBeDeleted = SavedCountryModel(getItem(position).code!!, getItem(position).name!!)
                clickListener.onItemClickDelete(countryWillBeDeleted)
            }else{
                holder.binding.saveButton.setBackgroundResource(R.drawable.ic_saved_star)
                val newSavedCountry = SavedCountryModel(getItem(position).code!!, getItem(position).name!!)
                clickListener.onItemClickSave(newSavedCountry)
            }
        }

        holder.binding.countryCard.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("countryCode", getItem(position).code)
            it.context.startActivity(intent)
        }

    }

    fun setSavedCountriesList(list: List<SavedCountryModel>){
        savedCountriesList.clear()
        savedCountriesList.addAll(list)
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