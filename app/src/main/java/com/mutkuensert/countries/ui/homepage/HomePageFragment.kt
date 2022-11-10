package com.mutkuensert.countries.ui.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutkuensert.countries.data.SavedCountryModel
import com.mutkuensert.countries.databinding.FragmentHomePageBinding
import com.mutkuensert.countries.ui.ItemClickListener
import com.mutkuensert.countries.util.Status
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomePageFragment"
@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomePageViewModel by activityViewModels()
    private lateinit var recyclerAdapter: HomePageRecyclerAdapter
    private val recyclerViewLayoutManager = LinearLayoutManager(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerAdapter()
        setObservers()
        viewModel.getAllSavedDataAndRefresh()
        if(viewModel.data.value!!.status != Status.SUCCESS) viewModel.requestCountries() //We must keep previous data during navigation between fragments.
        setLoadMoreListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerAdapter(){
        recyclerAdapter = HomePageRecyclerAdapter(object : ItemClickListener{
            override fun onItemClickSave(savedCountryModel: SavedCountryModel) {
                viewModel.saveData(savedCountryModel)
            }

            override fun onItemClickDelete(savedCountryModel: SavedCountryModel) {
                viewModel.deleteSavedData(savedCountryModel)

            }

        })

        binding.recyclerView.layoutManager = recyclerViewLayoutManager
        binding.recyclerView.adapter = recyclerAdapter
    }

    private fun setObservers(){
        viewModel.data.observe(viewLifecycleOwner){
            when(it.status){
                Status.STANDBY -> {}
                Status.ERROR -> {
                    binding.progressBarLoadingMore.visibility = View.GONE
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    binding.loadMoreButton.visibility = View.GONE
                    binding.progressBarLoadingMore.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBarLoadingMore.visibility = View.GONE
                    binding.loadMoreButton.visibility = View.VISIBLE
                    recyclerAdapter.submitList(it.data)
                }
            }
        }

        viewModel.savedCountries.observe(viewLifecycleOwner){
            recyclerAdapter.setSavedCountriesList(it)
        }
    }

    private fun setLoadMoreListener(){
        binding.loadMoreButton.setOnClickListener {
            viewModel.getCountriesNextPage()
            it.visibility = View.GONE
            binding.progressBarLoadingMore.visibility = View.VISIBLE
        }
    }
}