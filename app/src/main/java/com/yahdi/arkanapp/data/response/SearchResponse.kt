package com.yahdi.arkanapp.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("matches")
    val matches: List<AyahResponse>
)
