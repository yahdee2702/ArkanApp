package com.yahdi.arkanapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahdi.arkanapp.data.network.QuranApiConfig
import com.yahdi.arkanapp.data.repository.QuranRepository
import com.yahdi.arkanapp.data.response.SearchResponse
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val repository = QuranRepository(QuranApiConfig.getApiService())

    val searchData = MutableLiveData<SearchResponse?>()

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