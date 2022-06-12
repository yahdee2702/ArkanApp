package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.viewModel.QuranViewModel
import com.yahdi.arkanapp.databinding.FragmentAyahBinding
import com.yahdi.arkanapp.utils.LoadingManager
import com.yahdi.arkanapp.utils.Utils

class AyahFragment : Fragment() {
    private var _binding: FragmentAyahBinding? = null
    private val binding get() = _binding as FragmentAyahBinding
    private val args: AyahFragmentArgs by navArgs()
    private val quranViewModel: QuranViewModel by viewModels()

    private val nextAyah = MutableLiveData<AyahResponse?>()
    private val previousAyah = MutableLiveData<AyahResponse?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAyahBinding.inflate(layoutInflater, container, false)
        setData(args.ayahData)

        binding.btnNextAyah.setOnClickListener {
            if (nextAyah.value != null) {
                setData(nextAyah.value!!)
            }
        }

        binding.btnPreviousAyah.setOnClickListener {
            if (previousAyah.value != null) {
                setData(previousAyah.value!!)
            }
        }

        return binding.root
    }

    private fun setupNextAndBack() {
        val currentId = args.ayahData.id
        val nextId = if (currentId in 1..6235) currentId+1 else 1
        val previousId = if (currentId in 2..6236) currentId-1 else 6236
        quranViewModel.getAyahFromId(nextId).observe(viewLifecycleOwner) {
            nextAyah.value = it
        }
        if (previousAyah.value == null) {
            quranViewModel.getAyahFromId(previousId).observe(viewLifecycleOwner) {
                previousAyah.value = it
            }
        } else {
            previousAyah.value = args.ayahData
        }
    }

    private fun setData(ayahData: AyahResponse) {
        ayahData.let { data ->
            binding.include.tvAyahArabic.text = getString(R.string.txt_quran_content).format(data.content, data.idInSurah)
            binding.include.tvAyahTranslate.text = data.translation
            binding.include.tvAyahLatin.text = data.transliteration
        }
        setupNextAndBack()
    }
}