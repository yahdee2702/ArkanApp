package com.yahdi.arkanapp.ui.fragments.quran_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.data.response.AyahRangeData
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.viewModel.AyahViewModel
import com.yahdi.arkanapp.databinding.FragmentAyahBinding
import com.yahdi.arkanapp.ui.QuranActivity
import com.yahdi.arkanapp.utils.Utils

class AyahFragment : Fragment() {
    private var _binding: FragmentAyahBinding? = null
    private val binding get() = _binding as FragmentAyahBinding
    private val args: AyahFragmentArgs by navArgs()
    private val ayahViewModel: AyahViewModel by viewModels()
    private var currentAyah: AyahRangeData? = null
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
            currentAyah?.goNext()
            updateData()
        }

        binding.btnPreviousAyah.setOnClickListener {
            currentAyah?.goPrevious()
            updateData()
        }
    }

    private fun updateData() {
        currentAyah?.let {
            if (ayahViewModel.ayahData.value?.isLoading == true || ayahViewModel.ayahData.value == null) return@let
            ayahViewModel.getAyahFromId(it.current.id)
        }
    }

    private fun setList() {
        ayahViewModel.apply {
            getAyahFromId(ayahData.value?.response?.current?.id ?: args.ayahData.id)
            ayahData.observe(viewLifecycleOwner) {
                if (it.isSuccess && it.response != null) {
                    currentAyah = it.response
                    setData(it.response!!.current)
                }
            }
        }
    }

    private fun setData(data: AyahResponse) {
        mActivity.setTitle("${data.surah?.name} ${data.surah?.id}:${data.idInSurah}")
        binding.include.apply {
            tvAyahArabic.text = getString(R.string.txt_quran_content).format(Utils.removeBasmallah(data), data.idInSurah)
            tvAyahTranslate.text = data.translation
            tvAyahLatin.text = data.transliteration
        }
    }
}