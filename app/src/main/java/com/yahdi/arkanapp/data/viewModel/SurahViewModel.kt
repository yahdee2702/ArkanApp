package com.yahdi.arkanapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahdi.arkanapp.data.network.QuranApiConfig
import com.yahdi.arkanapp.data.repository.QuranRepository
import com.yahdi.arkanapp.data.response.SurahResponse
import kotlinx.coroutines.launch

class SurahViewModel: ViewModel() {
    private val repository = QuranRepository(QuranApiConfig.getApiService())
    val surahData = MutableLiveData<SurahResponse>()

    fun getSurahFromId(id: Int) {
        viewModelScope.launch {
            surahData.value = repository.getSurah(id)
        }
    }

}