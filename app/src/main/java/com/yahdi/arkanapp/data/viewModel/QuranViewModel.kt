package com.yahdi.arkanapp.data.viewModel

import androidx.lifecycle.*
import com.yahdi.arkanapp.data.network.QuranApi
import com.yahdi.arkanapp.data.network.QuranApiConfig
import com.yahdi.arkanapp.data.repository.QuranRepository
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.SearchResponse
import com.yahdi.arkanapp.data.response.SurahResponse
import kotlinx.coroutines.launch

class QuranViewModel: ViewModel() {
    private val api: QuranApi = QuranApiConfig.getApiService()
    private val repository: QuranRepository = QuranRepository(api)
    private val quranData = MutableLiveData<List<SurahResponse>>()
    val searchData = MutableLiveData<SearchResponse?>()

    fun getQuranData(): LiveData<List<SurahResponse>> {
        if (quranData.value == null) {
            viewModelScope.launch {
                quranData.value = repository.getQuran().data
            }
        }
        return quranData
    }

    fun getSurahFromId(id: Int): LiveData<SurahResponse> {
        val response = MutableLiveData<SurahResponse>()

        viewModelScope.launch {
            response.value = repository.getSurah(id)
        }

        return response
    }

    fun getAyahFromId(id: Int): LiveData<AyahResponse> {
        val response = MutableLiveData<AyahResponse>()

        viewModelScope.launch {
            response.value = repository.getAyah(id)
        }

        return response
    }

    fun searchBySurah(id: Int, keyword: String) {
        viewModelScope.launch {
            val res = repository.searchBySurah(id, keyword)
            if (res.isSuccessful) {
                searchData.value = res.body()
            }
        }
    }

    fun searchByAll(keyword: String) {
        viewModelScope.launch {
            val res = repository.searchByAll(keyword)
            if (res.isSuccessful) {
                searchData.value = res.body()
            }
        }
    }
}