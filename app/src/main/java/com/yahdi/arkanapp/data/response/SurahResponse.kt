package com.yahdi.arkanapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SurahResponse(
    @SerializedName("number")
    val id: Int,
    @SerializedName("name")
    val nameArabic: String,
    @SerializedName("englishName")
    val name: String,
    @SerializedName("ayahs")
    var ayahs: List<AyahResponse>? = null
): Parcelable
