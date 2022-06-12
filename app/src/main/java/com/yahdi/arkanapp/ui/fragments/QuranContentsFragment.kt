package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.SurahResponse
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentQuranContentsBinding
import com.yahdi.arkanapp.ui.adapters.SurahListAdapter
import com.yahdi.arkanapp.utils.LoadingManager
import com.yahdi.arkanapp.utils.Utils

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

        setHasOptionsMenu(true)
        initializeRecyclerView()
        setUpSearch()
        getData()

        return binding.root
    }

    private fun setUpSearch() {
        binding.svTitleSearch.apply {
            queryHint = getString(R.string.txt_search).format("surah title")
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()) {
                        val searched = surahList.filter {
                            Regex(newText, RegexOption.IGNORE_CASE).containsMatchIn(it.name)
                        }
                        surahListAdapter.setData(searched)
                    } else {
                        surahListAdapter.setData(surahList)
                    }
                    return false
                }

            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_quran, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                val action = QuranContentsFragmentDirections.actionQuranContentsFragmentToSearchFragment2(null)
                this.findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        if (!Utils.isOnline(requireContext())) return
        val loading = LoadingManager(viewLifecycleOwner, binding.progressMain)
        loading.isLoading.value = true
        quranViewModel.getQuranData().observe(viewLifecycleOwner) {
            loading.isLoading.value = false
            surahList = ArrayList(it)
            surahListAdapter.setData(surahList)
        }
    }
}