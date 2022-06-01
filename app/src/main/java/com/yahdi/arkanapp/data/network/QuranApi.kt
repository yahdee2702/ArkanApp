package com.yahdi.arkanapp.data.network

import com.yahdi.arkanapp.data.response.AyahResponse
import com.yahdi.arkanapp.data.response.QuranResponse
import com.yahdi.arkanapp.data.response.SurahResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApi {
    @GET("surah")
    suspend fun getQuran(): QuranResponse

    @GET("surah/{surahId}/editions/quran-simple,id.indonesian")
    suspend fun getSurah(@Path("surahId") id: Int): SurahResponse

    @GET("ayah/{ayahId}/editions/quran-simple,id.indonesian")
    suspend fun getAyah(@Path("ayahId") id:Int): AyahResponse
}