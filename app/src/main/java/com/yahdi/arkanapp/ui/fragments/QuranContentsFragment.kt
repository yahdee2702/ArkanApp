package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentQuranContentsBinding
import com.yahdi.arkanapp.ui.adapters.SurahListAdapter

class QuranContentsFragment : Fragment() {
    private var _binding: FragmentQuranContentsBinding? = null
    private val binding get() = _binding as FragmentQuranContentsBinding

    private val quranViewModel by viewModels<QuranViewModel>()
    private val surahListAdapter by lazy {
        SurahListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuranContentsBinding.inflate(inflater, container, false)

        initializeRecyclerView()
        getData()

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
            surahListAdapter.setData(it)
        }
    }
}