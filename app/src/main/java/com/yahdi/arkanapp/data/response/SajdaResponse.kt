package com.yahdi.arkanapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SajdaResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("recommended")
    val isRecommended: Boolean,
    @SerializedName("obligatory")
    val isObligatory: Boolean,
): Parcelable
