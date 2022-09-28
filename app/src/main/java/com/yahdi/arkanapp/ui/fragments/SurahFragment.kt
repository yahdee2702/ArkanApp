package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentSurahBinding
import com.yahdi.arkanapp.ui.adapters.AyahListAdapter
import com.yahdi.arkanapp.utils.LoadingManager
import com.yahdi.arkanapp.utils.Utils

class SurahFragment : Fragment() {
    private val navArgs by navArgs<SurahFragmentArgs>()

    private var _binding: FragmentSurahBinding? = null
    private val binding get() = _binding as FragmentSurahBinding

    private var _mLayoutManager: LinearLayoutManager? = null
    private val mLayoutManager get() = _mLayoutManager as LinearLayoutManager
    private val quranViewModel by viewModels<QuranViewModel>()
    private var ableToJump = false
    private var alreadyLoaded = false

    private val ayahAdapter by lazy {
        AyahListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurahBinding.inflate(layoutInflater, container, false)
        _mLayoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)
        initializeRecyclerView()
        getData()

        return binding.root
    }

    private fun initializeRecyclerView() {
        binding.rvAyahList.apply {
            // ViewCompat.setNestedScrollingEnabled(this, false)
            adapter = ayahAdapter
            layoutManager = mLayoutManager
            addItemDecoration(
                DividerItemDecoration(context, mLayoutManager.orientation)
            )
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    Log.d("Position", mLayoutManager.findFirstVisibleItemPosition().toString())
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
        if (navArgs.jumpTo != 0) {
            ableToJump = true
        }
    }

    private fun getData() {
        if (!Utils.isOnline(requireContext())) return
        if (alreadyLoaded) return

        alreadyLoaded = true

        val loading = LoadingManager(viewLifecycleOwner, binding.progressSurah)
        loading.isLoading.value = true

        quranViewModel.getSurahFromId(navArgs.surahData.id)
            .observe(viewLifecycleOwner) { response ->
                response.ayahs?.let {
                    val newList = it.map {ayah ->
                        ayah.surah = navArgs.surahData
                        ayah
                    }
                    ayahAdapter.setData(newList)
                    if (ableToJump) {
                        ableToJump = false
                        mLayoutManager.scrollToPositionWithOffset(navArgs.jumpTo - 1, 20)
                    }
                    loading.isLoading.value = false
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_quran, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                val action =
                    SurahFragmentDirections.actionSurahFragmentToSearchFragment(navArgs.surahData)
                this.findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}