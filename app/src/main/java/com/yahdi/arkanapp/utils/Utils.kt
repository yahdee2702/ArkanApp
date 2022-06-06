package com.yahdi.arkanapp.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.yahdi.arkanapp.data.response.AyahResponse
import java.text.DateFormat

object Utils {
    fun getDefaultGson(): GsonBuilder = GsonBuilder()
        .enableComplexMapKeySerialization()
        .serializeNulls()
        .setDateFormat(DateFormat.LONG)
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .setPrettyPrinting()
        .setVersion(1.0)

    fun removeBasmallah(ayahData: AyahResponse) :String {
        return if (ayahData.idInSurah != 1 && ayahData.idInSurah != 1336) ayahData.content.substring(36) else ayahData.content
    }
}