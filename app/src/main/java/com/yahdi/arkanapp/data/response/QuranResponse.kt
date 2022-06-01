package com.yahdi.arkanapp.data.response

import com.google.gson.annotations.SerializedName

data class QuranResponse(
    @SerializedName("data")
    val data: List<SurahResponse>,
)