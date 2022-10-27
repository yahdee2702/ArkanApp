package com.yahdi.arkanapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahdi.arkanapp.data.network.QuranApiConfig
import com.yahdi.arkanapp.data.repository.QuranRepository
import com.yahdi.arkanapp.data.response.AyahRangeData
import com.yahdi.arkanapp.data.response.ResponseResult
import kotlinx.coroutines.launch

class AyahViewModel: ViewModel() {
    private val repository = QuranRepository(QuranApiConfig.getApiService())

    val ayahData = MutableLiveData<ResponseResult<AyahRangeData>>()

    fun getAyahFromId(id: Int) {
        viewModelScope.launch {
            val response = ResponseResult<AyahRangeData>()
            try {
                ayahData.value = response.loading()
                val currentAyah = repository.getAyah(id)
                val currentId = currentAyah.id
                val previousAyah = repository.getAyah(if (currentId - 1 in 1..6236) currentId - 1 else 6236)
                val nextAyah = repository.getAyah(if (currentId + 1 in 1..6236) currentId + 1 else 1)

                ayahData.value = response.success(AyahRangeData(current = currentAyah, previous = previousAyah, next = nextAyah))
            } catch (e:Exception) {
                response.error(e.message.toString())
                e.printStackTrace()
            }
        }
    }
}