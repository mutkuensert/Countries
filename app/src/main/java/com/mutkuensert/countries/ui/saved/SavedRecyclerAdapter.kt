package com.mutkuensert.countries.ui.saved

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mutkuensert.countries.R
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.databinding.SingleItemBinding
import com.mutkuensert.countries.ui.ItemClickListener
import com.mutkuensert.countries.ui.detail.DetailActivity

class SavedRecyclerAdapter(private val clickListener: ItemClickListener): ListAdapter<SavedCountryModel, SavedRecyclerAdapter.ViewHolder>(SavedCountryModelDiffCallback) {
    class ViewHolder(val binding: SingleItemBinding): RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = getItem(position).countryName
        holder.binding.saveButton.setBackgroundResource(R.drawable.ic_saved_star)

        holder.binding.saveButton.setOnClickListener {
            if(currentList.contains(getItem(position))){
                holder.binding.saveButton.setBackgroundResource(R.drawable.ic_unsaved_star)
                val countryWillBeDeleted= SavedCountryModel(getItem(position).countryId, getItem(position).countryName)
                clickListener.onItemClickDelete(countryWillBeDeleted)
            }else{
                holder.binding.saveButton.setBackgroundResource(R.drawable.ic_saved_star)
                val newSavedCountry = SavedCountryModel(getItem(position).countryId, getItem(position).countryName)
                clickListener.onItemClickSave(newSavedCountry)
            }
        }

        holder.binding.countryCard.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("countryCode", getItem(position).countryId)
            it.context.startActivity(intent)
        }
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