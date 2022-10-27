package com.yahdi.arkanapp.data.viewModel

import androidx.lifecycle.*
import com.yahdi.arkanapp.data.network.QuranApi
import com.yahdi.arkanapp.data.network.QuranApiConfig
import com.yahdi.arkanapp.data.repository.QuranRepository
import com.yahdi.arkanapp.data.response.SurahResponse
import kotlinx.coroutines.launch

class QuranViewModel: ViewModel() {
    private val api: QuranApi = QuranApiConfig.getApiService()
    private val repository: QuranRepository = QuranRepository(api)

    val quranData = MutableLiveData<List<SurahResponse>>()

    fun getQuranData() {
        if (quranData.value == null) {
            viewModelScope.launch {
                quranData.value = repository.getQuran().data
            }
        }
    }
}