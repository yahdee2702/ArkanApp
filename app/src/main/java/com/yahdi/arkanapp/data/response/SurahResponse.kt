package com.yahdi.arkanapp.data.response

import com.google.gson.annotations.SerializedName

data class SurahResponse(
    @SerializedName("number")
    val id: Int,
    @SerializedName("name")
    val nameArabic: String,
    @SerializedName("englishName")
    val name: String,
)
