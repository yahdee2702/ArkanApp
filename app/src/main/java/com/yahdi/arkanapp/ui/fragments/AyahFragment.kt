package com.yahdi.arkanapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yahdi.arkanapp.R
import com.yahdi.arkanapp.databinding.FragmentAyahBinding

class AyahFragment : Fragment() {
    private var _binding: FragmentAyahBinding? = null
    private val binding get() = _binding as FragmentAyahBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAyahBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}