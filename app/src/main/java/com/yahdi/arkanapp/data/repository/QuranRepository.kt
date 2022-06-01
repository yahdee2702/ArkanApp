package com.yahdi.arkanapp.data.repository

import com.yahdi.arkanapp.data.network.QuranApi
import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.QuranResponse
import com.yahdi.arkanapp.data.response.SurahResponse
import retrofit2.Call

class QuranRepository(private val api: QuranApi) {
    suspend fun getQuran(): QuranResponse {
        return api.getQuran()
    }

    suspend fun getSurah(id: Int): SurahResponse {
        return api.getSurah(id)
    }

    suspend fun getAyah(id: Int): AyahResponse {
        return api.getAyah(id)
    }
}