package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentAyahBinding
import com.yahdi.arkanapp.ui.QuranActivity
import com.yahdi.arkanapp.utils.Utils

class AyahFragment : Fragment() {
    private var _binding: FragmentAyahBinding? = null
    private val binding get() = _binding as FragmentAyahBinding
    private val args: AyahFragmentArgs by navArgs()
    private val quranViewModel: QuranViewModel by viewModels()
    private val ayahList = mutableListOf<AyahResponse?>(null, null, null, null)
    private lateinit var mActivity: QuranActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAyahBinding.inflate(layoutInflater, container, false)
        mActivity = activity as QuranActivity
        setList()
        setUpButtons()

        return binding.root
    }

    private fun setUpButtons() {
        binding.btnNextAyah.setOnClickListener {
            if (ayahList[0] != null) {
                ayahList[2] = ayahList[1]
                ayahList[1] = ayahList[0]
                ayahList[0] = null
                setList()
            }
        }

        binding.btnPreviousAyah.setOnClickListener {
            if (ayahList[2] != null) {
                ayahList[0] = ayahList[1]
                ayahList[1] = ayahList[2]
                ayahList[2] = null
                setList()
            }
        }
    }

    private fun setList() {
        if (ayahList[1] == null) {
            ayahList[1] = args.ayahData
        }
        val currentId = ayahList[1]!!.id
        val nextAyah = if (currentId in 1..6235) currentId + 1 else 1
        val previousAyah = if(currentId in 2..6236) currentId - 1 else 6236

        if (ayahList[0] == null) {
            quranViewModel.getAyahFromId(nextAyah).observe(viewLifecycleOwner) {
                ayahList[0] = it
            }
        }

        if (ayahList[2] == null) {
            quranViewModel.getAyahFromId(previousAyah).observe(viewLifecycleOwner) {
                ayahList[2] = it
            }
        }

        setData()
    }

    private fun setData() {
        ayahList[1]?.let { data ->
            mActivity.setTitle("${data.surah?.name} ${data.surah?.id}:${data.idInSurah}")
            binding.include.apply {
                tvAyahArabic.text = getString(R.string.txt_quran_content).format(Utils.removeBasmallah(data), data.idInSurah)
                tvAyahTranslate.text = data.translation
                tvAyahLatin.text = data.transliteration
            }
        }
    }
}