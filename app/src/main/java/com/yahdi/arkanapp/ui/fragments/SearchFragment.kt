package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentSearchBinding
import com.yahdi.arkanapp.ui.adapters.AyahListAdapter
import com.yahdi.arkanapp.ui.adapters.SearchListAdapter

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding as FragmentSearchBinding

    private val searchListAdapter by lazy {
        SearchListAdapter()
    }
    private val quranViewModel by viewModels<QuranViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.svSurahSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    quranViewModel.searchByAll(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        binding.rvSearchList.apply {
            adapter = searchListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        quranViewModel.searchData.observe(viewLifecycleOwner) {
            searchListAdapter.setData(it)
        }

        return binding.root
    }
}