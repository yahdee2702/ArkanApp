package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentQuranContentsBinding
import com.yahdi.arkanapp.ui.adapters.SurahListAdapter

class QuranContentsFragment : Fragment() {
    private var _binding: FragmentQuranContentsBinding? = null
    private val binding get() = _binding as FragmentQuranContentsBinding

    private val quranViewModel by viewModels<QuranViewModel>()
    private val surahListAdapter by lazy {
        SurahListAdapter()
    }

    private var surahList: ArrayList<SurahResponse> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuranContentsBinding.inflate(inflater, container, false)

        initializeRecyclerView()
        getData()

        binding.svSurahSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    val searched = surahList.filter {
                        Regex("(?:$newText)", RegexOption.IGNORE_CASE).containsMatchIn(it.name)
                    }
                    surahListAdapter.setData(searched)
                } else {
                    surahListAdapter.setData(surahList)
                }
                return false
            }

        })

        binding.btnSearch.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_quranContentsFragment_to_searchFragment2)
        }

        return binding.root
    }

    private fun initializeRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context)
        binding.rvQuran.apply {
            adapter = surahListAdapter
            layoutManager = mLayoutManager
            addItemDecoration(
                DividerItemDecoration(context, mLayoutManager.orientation)
            )
        }
    }

    private fun getData() {
        quranViewModel.getQuranData().observe(viewLifecycleOwner) {
            surahList = ArrayList(it)
            surahListAdapter.setData(surahList)
        }
    }
}