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
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentSurahBinding
import com.yahdi.arkanapp.ui.QuranActivity
import com.yahdi.arkanapp.ui.adapters.AyahListAdapter

class SurahFragment : Fragment() {
    private val navArgs by navArgs<SurahFragmentArgs>()

    private var _binding: FragmentSurahBinding? = null
    private val binding get() = _binding as  FragmentSurahBinding

    private var _mainActivity: QuranActivity? = null
    private val mainActivity get() = _mainActivity as QuranActivity

    private var _mLayoutManager: LinearLayoutManager? = null
    private val mLayoutManager get() = _mLayoutManager as LinearLayoutManager

    private val quranViewModel by viewModels<QuranViewModel>()

    private var ableToJump = false

    private val ayahAdapter by lazy {
        AyahListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahBinding.inflate(layoutInflater, container, false)
        _mainActivity = activity as QuranActivity
        _mLayoutManager = LinearLayoutManager(context)

        mainActivity.setTitle(navArgs.surahData.name)

        initializeRecyclerView()
        getData()

        return binding.root
    }

    private fun initializeRecyclerView() {
        binding.rvAyahList.apply {
            adapter = ayahAdapter
            layoutManager = mLayoutManager
            addItemDecoration(
                DividerItemDecoration(context, mLayoutManager.orientation)
            )
        }
        if (navArgs.jumpTo != 0) {
            ableToJump = true
        }
    }

    private fun getData() {
        quranViewModel.getSurahFromId(navArgs.surahData.id).observe(viewLifecycleOwner) { response ->
            response.ayahs.let {
                    ayahAdapter.setData(it!!)
                if (ableToJump) {
                    ableToJump = false
                    mLayoutManager.scrollToPositionWithOffset(navArgs.jumpTo-1, 20)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.setTitle("Al-Qur'an")
        _mainActivity = null
        _binding = null
    }
}