package com.mutkuensert.countries.ui.homepage

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.countries.R
import com.mutkuensert.countries.data.countries.CountriesDataModel
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.data.countries.CountriesDataAndExistenceInDatabaseModel
import com.mutkuensert.countries.databinding.SingleItemBinding
import com.mutkuensert.countries.ui.ItemClickListener
import com.mutkuensert.countries.ui.detail.DetailActivity

class HomePageRecyclerAdapter(private val clickListener: ItemClickListener): ListAdapter<CountriesDataAndExistenceInDatabaseModel, HomePageRecyclerAdapter.ViewHolder>(CountriesDataAndExistenceInDatabaseModelDiffCallback) {

    class ViewHolder(val binding: SingleItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = getItem(position).data.name

        if(getItem(position).existenceInDatabase){
            holder.binding.saveButton.setBackgroundResource(R.drawable.ic_saved_star)
        }else{
            holder.binding.saveButton.setBackgroundResource(R.drawable.ic_unsaved_star)
        }

        holder.binding.saveButton.setOnClickListener {
            if(getItem(position).existenceInDatabase){
                holder.binding.saveButton.setBackgroundResource(R.drawable.ic_unsaved_star)

                if(getItem(position).data.code != null && getItem(position).data.name != null){
                    val countryWillBeDeleted = SavedCountryModel(getItem(position).data.code!!, getItem(position).data.name!!)
                    clickListener.onItemClickDelete(countryWillBeDeleted, position)
                }else{
                    Toast.makeText(holder.binding.countryCard.context, "Missing data", Toast.LENGTH_LONG).show()
                }
            }else{
                holder.binding.saveButton.setBackgroundResource(R.drawable.ic_saved_star)

                if(getItem(position).data.code != null && getItem(position).data.name != null){
                    val newSavedCountry = SavedCountryModel(getItem(position).data.code!!, getItem(position).data.name!!)
                    clickListener.onItemClickSave(newSavedCountry, position)
                }else{
                    Toast.makeText(holder.binding.countryCard.context, "Missing data", Toast.LENGTH_LONG).show()
                }
            }
        }

        holder.binding.countryCard.setOnClickListener {
            clickListener.onItemNameClick(position)
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("countryCode", getItem(position).data.code)
            it.context.startActivity(intent)
        }

    }

    object CountriesDataAndExistenceInDatabaseModelDiffCallback: DiffUtil.ItemCallback<CountriesDataAndExistenceInDatabaseModel>(){
        override fun areItemsTheSame(
            oldItem: CountriesDataAndExistenceInDatabaseModel,
            newItem: CountriesDataAndExistenceInDatabaseModel
        ): Boolean {
            return oldItem.data.name == newItem.data.name
        }

        override fun areContentsTheSame(
            oldItem: CountriesDataAndExistenceInDatabaseModel,
            newItem: CountriesDataAndExistenceInDatabaseModel
        ): Boolean {
            return oldItem.existenceInDatabase == newItem.existenceInDatabase
        }

    }
}