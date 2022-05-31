package com.yahdi.arkanapp.data.response

import com.google.gson.annotations.SerializedName

data class SajdaResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("recommended")
    val isRecommended: Boolean,
    @SerializedName("obligatory")
    val isObligatory: Boolean,
)
