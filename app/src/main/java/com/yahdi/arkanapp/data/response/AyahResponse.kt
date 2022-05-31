package com.yahdi.arkanapp.data.response

import com.google.gson.annotations.SerializedName

data class AyahResponse(
    @SerializedName("number")
    val id: Int,
    @SerializedName("numberInSurah")
    val idInSurah: Int,
    @SerializedName("text")
    val content: String,
    @SerializedName("sajda")
    val sajda: SajdaResponse? = null,
    var translation: String?

)
