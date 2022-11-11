package com.mutkuensert.countries.ui.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.databinding.FragmentSavedBinding
import com.mutkuensert.countries.ui.ItemClickListener
import com.mutkuensert.countries.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerAdapter: SavedRecyclerAdapter
    private val recyclerViewLayoutManager = LinearLayoutManager(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerAdapter()
        setObserver()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllSavedDataAndRefresh()
        viewModel.checkExistencesInDatabase()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setObserver(){
        viewModel.savedCountries.observe(viewLifecycleOwner){
            recyclerAdapter.submitList(it)
        }
    }

    private fun setRecyclerAdapter(){
        recyclerAdapter = SavedRecyclerAdapter(object : ItemClickListener {
            override fun onItemClickSave(savedCountryModel: SavedCountryModel, position: Int) {
                viewModel.saveData(savedCountryModel)
            }

            override fun onItemClickDelete(savedCountryModel: SavedCountryModel, position: Int) {
                viewModel.deleteSavedData(savedCountryModel)
            }

            override fun onItemNameClick(position: Int) {
                //
            }

        })

        binding.recyclerView.layoutManager = recyclerViewLayoutManager
        binding.recyclerView.adapter = recyclerAdapter
    }
}