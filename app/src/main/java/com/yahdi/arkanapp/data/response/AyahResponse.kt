package com.yahdi.arkanapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AyahResponse(
    @SerializedName("number")
    val id: Int,
    @SerializedName("numberInSurah")
    val idInSurah: Int,
    @SerializedName("text")
    val content: String,
//    @SerializedName("sajda")
//    val sajda: SajdaResponse? = null,
    var translation: String?
): Parcelable
