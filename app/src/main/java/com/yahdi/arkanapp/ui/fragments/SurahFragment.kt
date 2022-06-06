package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.QuranViewModel
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.databinding.FragmentSurahBinding
import com.yahdi.arkanapp.ui.QuranActivity
import com.yahdi.arkanapp.ui.adapters.AyahListAdapter

class SurahFragment : Fragment() {
    private val navArgs by navArgs<SurahFragmentArgs>()

    private var _binding: FragmentSurahBinding? = null
    private val binding get() = _binding as  FragmentSurahBinding

    private var _mainActivity: QuranActivity? = null
    private val mainActivity get() = _mainActivity as QuranActivity

    private val quranViewModel by viewModels<QuranViewModel>()

    private val ayahAdapter by lazy {
        AyahListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahBinding.inflate(layoutInflater, container, false)
        _mainActivity = activity as QuranActivity

        val data = navArgs.surahData
        mainActivity.setTitle(data.name)

        initializeRecyclerView()
        getData(data)

        return binding.root
    }

    private fun initializeRecyclerView() {
        val mLayoutManager = LinearLayoutManager(context)

        binding.rvAyahList.apply {
            adapter = ayahAdapter
            layoutManager = mLayoutManager
            addItemDecoration(
                DividerItemDecoration(context, mLayoutManager.orientation)
            )
        }

        if (navArgs.jumpTo != -1) {
            mLayoutManager.scrollToPositionWithOffset(navArgs.jumpTo, 20)
        }
    }

    private fun getData(data: SurahResponse) {
        quranViewModel.getSurahFromId(data.id).observe(viewLifecycleOwner) { response ->
            response.ayahs.let {
                    ayahAdapter.setData(it!!)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}