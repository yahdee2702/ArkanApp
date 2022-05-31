package com.yahdi.arkanapp.data.response

import com.google.gson.annotations.SerializedName

data class QuranApiResponse<T>(
    @SerializedName("data")
    val data: T,
)