package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentSearchBinding
import com.yahdi.arkanapp.ui.adapters.SearchListAdapter

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding as FragmentSearchBinding

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

        binding.svSurahSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    quranViewModel.searchByAll(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        binding.rvSearchList.apply {
            adapter = searchListAdapter
            layoutManager = mLayoutManager
            addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
        }

        quranViewModel.searchData.observe(viewLifecycleOwner) {
            if (it != null) {
                searchListAdapter.setData(it)
            } else {
                Toast.makeText(context, "Not found!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}