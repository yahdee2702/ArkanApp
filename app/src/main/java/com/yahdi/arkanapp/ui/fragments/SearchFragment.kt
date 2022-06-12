package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentSearchBinding
import com.yahdi.arkanapp.ui.adapters.SearchListAdapter
import com.yahdi.arkanapp.utils.LoadingManager
import com.yahdi.arkanapp.utils.Utils

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding as FragmentSearchBinding

    private var _loading: LoadingManager? = null
    private val loading get() = _loading as LoadingManager

    private val args: SearchFragmentArgs by navArgs()

    private val searchListAdapter by lazy {
        SearchListAdapter()
    }

    private var _mLayoutManager: LinearLayoutManager? = null
    private val mLayoutManager get() = _mLayoutManager as LinearLayoutManager

    private val quranViewModel by viewModels<QuranViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        _mLayoutManager = LinearLayoutManager(context)
        _loading = LoadingManager(viewLifecycleOwner, binding.progressSearch)

        setSearchField()
        setRecyclerView()
        setData()

        return binding.root
    }

    private fun setRecyclerView() {
        binding.rvSearchList.apply {
            adapter = searchListAdapter
            layoutManager = mLayoutManager
            addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
        }
    }

    private fun setSearchField() {
        binding.svSurahSearch.apply {
            queryHint = if (args.surahData != null) {
                getString(R.string.txt_search).format(args.surahData!!.name)
            } else {
                getString(R.string.txt_search).format("all")
            }
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        if (args.surahData != null ){
                            quranViewModel.searchBySurah(args.surahData!!.id, query)
                        } else {
                            quranViewModel.searchByAll(query)
                        }
                        loading.isLoading.value = true
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }
            })
        }
    }

    private fun setData() {
        if (!Utils.isOnline(requireContext())) return
        quranViewModel.searchData.observe(viewLifecycleOwner) {
            if (it != null) {
                searchListAdapter.setData(it)
                loading.isLoading.value = false
            } else {
                Toast.makeText(context, "Not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}